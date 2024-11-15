package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.yonghwan.myapp.domain.Notice;

@Data
public class NoticeDto {
    private Long id;
    private String title;
    private String content;

    public NoticeDto(){}

    public NoticeDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NoticeDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public NoticeDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }

    public Notice toEntity(){
        return Notice.builder()
                .title(this.title)
                .content(this.content).build();
    }
}
