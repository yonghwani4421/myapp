package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.BoardComment;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardComment> findCommentsByBoardId(Long boardId);

}
