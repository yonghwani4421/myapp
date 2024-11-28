package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.BoardAttachment;
import me.yonghwan.myapp.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {

    /**
     * 댓글 리스트 조회 단순히 flat 조회
     * @param boardId
     * @return
     */
    List<BoardComment> findByBoardId(@Param("boardId") Long boardId);

    /**
     * 최상위
     * @param boardId
     * @return
     */
    List<BoardComment> findByBoardIdAndParentCommentIsNull(Long boardId);
}
