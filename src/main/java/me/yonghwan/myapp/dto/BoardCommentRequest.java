package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardComment;
import me.yonghwan.myapp.domain.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentRequest {
    private Long parentId;
    private String content;


    public BoardComment toEntity(Member member, Board board, BoardComment boardComment){
       return new BoardComment(content,member,board,boardComment);
    }


}