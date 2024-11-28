package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardComment;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.BoardCommentRequest;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.repository.BoardCommentRepository;
import me.yonghwan.myapp.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final SessionUtil sessionUtil;

    /**
     * 댓글 작성
     * @param request
     * @param boardId
     * @return
     */
    public BoardComment createComment(BoardCommentRequest request,Long boardId){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + boardId));

        BoardComment parentComment = null;
       if (request.getParentId() != null){
           parentComment = boardCommentRepository.findById(request.getParentId())
                   .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getParentId())); // 부모 댓글이 없을 때 예외 발생
       }

        BoardComment saveBoardComment = request.toEntity(sessionUtil.getMemberSesson(), board, parentComment);

        return boardCommentRepository.save(saveBoardComment);
    }
}
