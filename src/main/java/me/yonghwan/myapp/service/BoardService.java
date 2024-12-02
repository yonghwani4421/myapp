package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardAttachment;
import me.yonghwan.myapp.domain.BoardLikes;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.BoardDetailResponse;
import me.yonghwan.myapp.dto.BoardRequest;
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

    /**
     * 게시판 등록 -> file이 있다면, s3에 저장후 DB에 저장
     * @param board
     * @param files
     * @return
     */
    @Transactional
    public Board saveBoardWithAttachments(Board board, List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            saveAttachments(board, files);
        }
        return boardRepository.save(board);
    }

    /**
     * 첨부파일 추가
     * @param board
     * @param files
     */
    public void saveAttachments(Board board, List<MultipartFile> files) {
        String[] uploadedFileUrls = s3Service.uploadFiles(files).split(",");
        List<Long> fileSize = files.stream().map(MultipartFile::getSize).collect(Collectors.toList());
        for (int i = 0; i < uploadedFileUrls.length; i++) {
             board.addAttachment(
                     new BoardAttachment(
                             fileUtil.getFileNameWithoutExtension(uploadedFileUrls[i]) // file name
                             , fileUtil.getFileExtension(uploadedFileUrls[i]) // file type
                             , fileSize.get(i) // file size
                             , uploadedFileUrls[i]) // file url

             );
         }
    }


    /**
     * 게시물 수정
     * @param request
     * @param files
     * @param id
     * @return
     */
    @Transactional
    public Board updateBoard(BoardRequest request, List<MultipartFile> files, Long id){
        Board board = findByIdWithAttachments(id);
        List<BoardAttachment> boardAttachments = board.getBoardAttachments();
        List<Long> deleteFileIds = request.getDeleteFileIds();

        /**
         * 제거될 파일 제거 S3에서도 제거
         */
        deleteS3Files(boardAttachments, deleteFileIds);
        /**
         * 변경된 내용 수정 변경감지
         */
        board.update(request);
        /**
         * 추가될 파일이 있는경우 추가
         */
        if (files != null && files.size() > 0){
            saveAttachments(board,files);
        }
        return board;
    }


    @Transactional
    public void changeStatus(Long id){
        findById(id).changeStatus();
    }

    private void deleteS3Files(List<BoardAttachment> boardAttachments, List<Long> deleteFileIds) {
        for (BoardAttachment boardAttachment : boardAttachments) {
            if(deleteFileIds.contains(boardAttachment.getId())){
                s3Service.deleteFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl()));
                boardAttachment.deleteBoard();
            }
        }
    }

    @Transactional
    public void deleteBoardById(Long id){
        Board board = findByIdWithAttachments(id);
        /**
         * 제거될 파일 제거 S3에서도 제거
         */
        deleteS3Files(
                board.getBoardAttachments()
                , board.getBoardAttachments()
                        .stream().map(BoardAttachment::getId)
                        .collect(Collectors.toList()));

        /**
         * Board 제거 CASCADE 설정으로 BoardAttachment 전부 같이 삭제
         */
        boardRepository.delete(board);

    }

    /**
     * 게시판 ID로 조회
     * @param id
     * @return
     */

    public Board findById(Long id){
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    /**
     * 게시판 ID로 첨부파일과 함께 조회
     * @param id
     * @return
     */
    public Board findByIdWithAttachments(Long id){
        return boardRepository.findByIdWithAttachments(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    /**
     * ID로 첨부파일 삭제
     * @param id
     */
    public void deleteAttachment(Long id){
        boardAttachmentRepository.deleteById(id);
    }

    /**
     * ID로 게시물 삭제
     * @param id
     */

    public void deleteById(Long id){
        boardRepository.deleteById(id);
    }

    /**
     * Board 테이블 리스트 전체 조회
     * @return
     */

    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    /**
     * Board_Attachment 테이블 리스트 전체 조회
     * @return
     */
    public List<BoardAttachment> findAttachmentAll(){
        return boardAttachmentRepository.findAll();
    }

    /**
     * 게시물 좋아요
     * @param boardId
     * @param member
     * @return
     */

    @Transactional
    public void addOrCancelLikes(Long boardId,Member member){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));

        if (isNotAlreadyLike(member,board)){
            boardLikesRepository.save(new BoardLikes(board, member));
        } else {
            boardLikesRepository.deleteByMemberAndBoard(member, board);
        }
    }

    /**
     * 게시물 좋아요 취소
     * @param boardId
     * @param memberId
     * @return
     */

    @Transactional
    public boolean cancelLikes(Long boardId, Long memberId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not found : "+ boardId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found : " + memberId));
        return boardLikesRepository.deleteByMemberAndBoard(member, board) > 0;
    }

    /**
     * 좋아요를 누른 멤버인지 확인
     * @param member
     * @param board
     * @return
     */
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
