package me.yonghwan.myapp.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.BoardComment;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.yonghwan.myapp.domain.QBoardComment.boardComment;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<BoardComment> findCommentsByBoardId(Long boardId) {
        return queryFactory
                .select(boardComment)
                .from(boardComment)
                .leftJoin(boardComment.childComments)
                .fetchJoin() // 자식 댓글 페치 조인
                .where(
                        boardComment.board.id.eq(boardId) // 특정 게시글의 댓글만
                )
                .orderBy(boardComment.parentComment.id.asc().nullsFirst(),
                        boardComment.createDate.asc()) // 원하는 정렬 추가 (예: ID 기준 정렬)
                .fetch();
    }
}
