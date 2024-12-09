package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.*;
import me.yonghwan.myapp.dto.BoardRequest;
import me.yonghwan.myapp.dto.PostRequest;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.PostLikesRepository;
import me.yonghwan.myapp.repository.PostPhotoRepository;
import me.yonghwan.myapp.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final S3Service s3Service;
    private final FileUtil fileUtil;
    private final PostRepository postRepository;
    private final PostPhotoRepository postPhotoRepository;
    private final PostLikesRepository postLikesRepository;

    /**
     * 거래 게시물 저장
     * @param post
     * @param files
     * @return
     */
    @Transactional
    public Post savePostWithAttachment(Post post, List<MultipartFile> files ){
        if (files != null && !files.isEmpty()) {
            saveAttachments(post, files);
        }
        return postRepository.save(post);
    }

    /**
     * 첨부파일 추가
     * @param post
     * @param files
     */
    @Transactional
    public void saveAttachments(Post post, List<MultipartFile> files) {
        String[] uploadedFileUrls = s3Service.uploadFiles(files).split(",");
        List<Long> fileSize = files.stream().map(MultipartFile::getSize).collect(Collectors.toList());
        for (int i = 0; i < uploadedFileUrls.length; i++) {
            post.addAttachment(
                    new PostPhoto(
                            fileUtil.getFileNameWithoutExtension(uploadedFileUrls[i]) // file name
                            , fileSize.get(i) // file size
                            , uploadedFileUrls[i]
                    )
            );
        }
    }


    /**
     * 거래 게시물 + 첨부파일
     * @param postId
     * @return
     */
    public Post findByIdWithPhotos(Long postId) {
        return postRepository.findByIdWithPhotos(postId).orElseThrow(() -> new IllegalArgumentException("not found : " + postId));
    }

    /**
     * 거래 게시물 수정
     * @param request
     * @param files
     * @param postId
     * @return
     */

    @Transactional
    public Post updatePost(PostRequest request, List<MultipartFile> files, Long postId) {
        Post post = findByIdWithPhotos(postId);
        List<PostPhoto> photos = post.getPhotos();
        List<Long> deleteFileIds = request.getDeleteFileIds();

        /**
         * 제거될 파일 제거 S3에서도 제거
         */
        deleteS3Files(photos, deleteFileIds);
        /**
         * 변경된 내용 수정 변경감지
         */
        post.update(request);
        /**
         * 추가될 파일이 있는경우 추가
         */
        if (files != null && files.size() > 0){
            saveAttachments(post,files);
        }
        return post;

    }
    private void deleteS3Files(List<PostPhoto> photos, List<Long> deleteFileIds) {
        for (PostPhoto photo : photos) {
            if(deleteFileIds.contains(photo.getId())){
                s3Service.deleteFile(fileUtil.convertToFileName(photo.getPhotoUrl()));
//                photos.remove(photo);
            }
        }
    }


    /**
     * 거래 게시물 삭제 + aws 에서 삭제
     * @param id
     */
    public void deletePost(Long id) {
        List<PostPhoto> photo = postPhotoRepository.findByPostId(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        List<Long> deleteFileIds = new ArrayList<>();
        photo.stream().forEach(postPhoto -> {
            deleteFileIds.add(postPhoto.getId());
        });

        // aws 삭제
        deleteS3Files(photo, deleteFileIds);

        // 나머지 테이블에서 삭제 연쇄삭제 됨
        postRepository.deleteById(id);
    }

    public Boolean existsByPostId(Long postId) {
        return postRepository.existsById(postId);
    }

    /**
     * 게시물 좋아요 / 좋아요 취소
     * @param post
     * @param member
     */
    @Transactional
    public void addOrCancelLikes(Post post, Member member) {
        if (!existsByMemberAndPost(member,post)){
            postLikesRepository.save(new PostLikes(post,member));
        } else {
            postLikesRepository.deleteByMemberAndPost(member,post);
        }
    }

    public Boolean existsByMemberAndPost(Member member,Post post){
        return postLikesRepository.existsByMemberAndPost(member,post);
    }

    public Long countByPost(Post post){
       return postLikesRepository.countByPost(post).orElseThrow(()-> new IllegalArgumentException("not found : " + post.getId()));
    }
}
