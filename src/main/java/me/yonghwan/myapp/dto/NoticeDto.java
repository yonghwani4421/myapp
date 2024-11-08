package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Notice;

@Data
public class NoticeDto {
    private Long id;
    private String title;
    private String content;

    public NoticeDto(){}

    public NoticeDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Notice toEntity(){
        return Notice.builder()
                .title(this.title)
                .content(this.content).build();
    }
}
