package me.yonghwan.myapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDate;
    public BoardListResponse(Long id, String title, String content, LocalDateTime createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
    }
}
