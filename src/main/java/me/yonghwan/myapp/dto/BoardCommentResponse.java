package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.yonghwan.myapp.domain.BoardComment;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardCommentResponse {
    private Long id;
    private String content;
    private Long memberId;
    private List<BoardCommentResponse> childComments;


    public BoardCommentResponse(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.content = boardComment.getContent();
        this.memberId = boardComment.getMember().getId();
        // 자식 댓글을 재귀적으로 BoardCommentResponse로 변환
        this.childComments = boardComment.getChildComments().stream()
                .map(BoardCommentResponse::new) // BoardComment -> BoardCommentResponse 변환
                .collect(Collectors.toList());
    }
}