package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardAttachment;
import me.yonghwan.myapp.domain.BoardLikes;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.BoardAttachmentRepository;
import me.yonghwan.myapp.repository.BoardLikesRepository;
import me.yonghwan.myapp.repository.BoardRepository;
import me.yonghwan.myapp.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardAttachmentRepository boardAttachmentRepository;
    private final FileUtil fileUtil;
    private final S3Service s3Service;

    private final BoardLikesRepository boardLikesRepository;
    private final MemberRepository memberRepository;
    public Board save(Board board){
        return boardRepository.save(board);
    }

    @Transactional
    public Board CreateBoardWithAttachmentSave(Board board, List<MultipartFile> files){

        if (files != null && !files.isEmpty()){
            String uploadedFileUrls = s3Service.uploadFiles(files);
            List<Long> fileSize = files.stream().map(MultipartFile::getSize).collect(Collectors.toList());

            for (int i = 0; i < uploadedFileUrls.split(",").length; i++) {
                String uploadedFileUrl = uploadedFileUrls.split(",")[i];  // 각 파일 URL
                String fileName = fileUtil.getFileNameWithoutExtension(uploadedFileUrl);
                String type = fileUtil.getFileExtension(uploadedFileUrl);
                long size = fileSize.get(i);  // fileSizes 리스트에서 해당 파일의 크기 추출

                BoardAttachment boardAttachment = new BoardAttachment(fileName, type, size, uploadedFileUrl);
                board.addAttachment(boardAttachment);
            }
        }

        return boardRepository.save(board);
    }

    public Board findByIdWithAttachments(Long id){
        return boardRepository.findByIdWithAttachments(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void deleteAttachment(Long id){
        boardAttachmentRepository.deleteById(id);
    }

    public void deleteById(Long id){
        boardRepository.deleteById(id);
    }

    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    public List<BoardAttachment> findAttachmentAll(){
        return boardAttachmentRepository.findAll();
    }

    public boolean addLikes(Long boardId, Long memberId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));

        if (isNotAlreadyLike(member,board)){
            boardLikesRepository.save(new BoardLikes(board, member));
            return true;
        }

        return false;
    }

    public boolean cancelLikes(Long boardId, Long memberId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));
        return boardLikesRepository.deleteByMemberAndBoard(member, board) > 0;
    }

    private boolean isNotAlreadyLike(Member member, Board board) {
        return boardLikesRepository.findByMemberAndBoard(member,board).isEmpty();
    }

    /**\
     * 게시판 페이징된 리스트 조회
     */
    public Page<Board> getBoardList(Pageable pageable){
         return boardRepository.findAll(pageable);
    }


}
