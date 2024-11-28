package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.Member;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequest {

    private String title;
    private String content;

    public Board toEntity(Member member){
        return new Board(title,content,member);
    }

}
