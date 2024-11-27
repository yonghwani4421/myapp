package me.yonghwan.myapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListResponse {
    private String title;
    private String content;
    private LocalDateTime createDate;
    public BoardListResponse(String title, String content, LocalDateTime createDate) {
        this.title = title;
        this.content = content;
        this.createDate = createDate;
    }
}
