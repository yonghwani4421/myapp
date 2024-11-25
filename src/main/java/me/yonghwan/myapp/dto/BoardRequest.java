package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Board;

@Data
public class BoardRequest {

    private String title;
    private String content;

    public Board toEntity(){
        return new Board(title,content);
    }

}
