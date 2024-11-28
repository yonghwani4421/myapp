package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<BoardComment> childComments = new ArrayList<>();

    public BoardComment(String content, Member member, Board board, BoardComment parentCommnet) {
        this.content = content;
        this.member = member;
        this.board = board;
        this.parentComment = parentCommnet;
    }

    /**
     * 대댓글을 추가하는 연관관계 편의 메서드 (부모 댓글이 없으면 일반 댓글)
     */
    public void addChildComment(BoardComment childComment) {
        this.childComments.add(childComment);
        childComment.setParentComment(this);
    }

    /**
     * 부모 댓글 설정
     * @param parentComment
     */
    private void setParentComment(BoardComment parentComment) {
        this.parentComment = parentComment;
    }
}
