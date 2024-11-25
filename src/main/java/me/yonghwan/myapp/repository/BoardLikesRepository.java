package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardLikes;
import me.yonghwan.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikesRepository extends JpaRepository<BoardLikes,Long> {
    Optional<BoardLikes> findByMemberAndBoard(Member member, Board board);

    int deleteByMemberAndBoard(Member member, Board board);
}
