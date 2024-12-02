package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardComment;
import me.yonghwan.myapp.dto.BoardCommentRequest;
import me.yonghwan.myapp.dto.BoardCommentResponse;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.repository.BoardCommentRepository;
import me.yonghwan.myapp.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Transactional
    public BoardComment createComment(BoardCommentRequest request, Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + boardId));
        BoardComment parentComment = null;
        if (request.getParentId() != null){
           parentComment = boardCommentRepository.findById(request.getParentId())
                   .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getParentId())); // 부모 댓글이 없을 때 예외 발생
        }
        return boardCommentRepository.save(request.toEntity(sessionUtil.getMemberSesson(), board, parentComment));
    }

    /**
     * 댓글 단순 flat 조회
     * @param boardId
     * @return
     */
    public List<BoardComment> getCommentFlatList(Long boardId){
        return boardCommentRepository.findByBoardId(boardId);
    }

    /**
     * id 로 댓글 조회
     * @param commentId
     * @return
     */
    public BoardComment findById(Long commentId){
        return boardCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found : " + commentId));
    }

    /**
     * 계층구조 댓글 리스트 전부 조회
     * @param boardId
     * @return
     */
    public List<BoardCommentResponse> getCommentsByBoardId(Long boardId) {
        List<BoardComment> comments = boardRepository.findCommentsByBoardId(boardId);
        List<BoardCommentResponse> commentResponses = new ArrayList<>();

        Map<Long, BoardCommentResponse> map = new HashMap<>();

        comments.stream().forEach(c -> {
            BoardCommentResponse response = new BoardCommentResponse(c);
            map.put(response.getId(), response);
            if(c.getParentComment() != null) map.get(c.getParentComment().getId()).getChildComments().add(response);
            else commentResponses.add(response);
        });

        // 부모 댓글 리스트를 DTO로 변환
        return commentResponses;
    }



}
