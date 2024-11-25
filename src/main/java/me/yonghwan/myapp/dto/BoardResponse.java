package me.yonghwan.myapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardResponse {
    private String title;
    private String content;

    private List<BoardAttachmentResponse> attachment;

    public BoardResponse(String title, String content, List<BoardAttachmentResponse> attachment) {
        this.title = title;
        this.content = content;
        this.attachment = attachment;
    }
}
