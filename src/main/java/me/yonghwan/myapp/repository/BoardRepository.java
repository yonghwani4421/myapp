package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * 게시물 첨부파일과 함께 조회
     * @param boardId
     * @return
     */
    @Query("select b from Board b" +
            " left join fetch" +
            " b.boardAttachments" +
            " where b.id = :boardId")
    Optional<Board> findByIdWithAttachments(@Param("boardId") Long boardId);

    @Query("select b from Board b where b.status = 'Y'")
    Page<Board> findAll(Pageable pageable);

}
