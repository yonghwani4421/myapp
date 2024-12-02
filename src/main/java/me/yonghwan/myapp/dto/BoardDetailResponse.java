package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Board;

import java.util.List;

@Data
public class BoardDetailResponse {

    private Long id;
    private String title;
    private String content;
    private int likeCount;
    private String writerName;

    private List<BoardAttachmentResponse> attachment;

    private List<BoardCommentResponse> comments;


    public BoardDetailResponse(Board board, String writerName, List<BoardAttachmentResponse> attachment, List<BoardCommentResponse> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.likeCount = board.getBoardLikes().size();
        this.writerName = writerName;
        this.attachment = attachment;
        this.comments = comments;
    }
}
