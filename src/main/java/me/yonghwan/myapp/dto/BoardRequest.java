package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.yonghwan.myapp.domain.Board;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardRequest {

    private String title;
    private String content;
    private List<Long> deleteFileIds;

    public BoardRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board toEntity(){
        return new Board(title,content);
    }

}
