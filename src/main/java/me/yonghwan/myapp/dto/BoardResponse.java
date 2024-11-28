package me.yonghwan.myapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardResponse {
    private Long id;
    private String title;
    private String content;

    private List<BoardAttachmentResponse> attachment;

    public BoardResponse(Long id, String title, String content, List<BoardAttachmentResponse> attachment) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
    }
}
