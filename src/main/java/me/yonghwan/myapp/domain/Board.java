package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.config.exception.DeletionException;
import me.yonghwan.myapp.dto.BoardRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    @Lob
    private String content;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * BoardAttachment
     * 1 : N 관계
     */
    @OneToMany(mappedBy = "board", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<BoardAttachment> boardAttachments = new ArrayList<>();

    /**
     * BoardLikes
     * 1 : N 관계
     */
    @OneToMany(mappedBy = "board", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<BoardLikes> boardLikes = new ArrayList<>();

    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.status = "Y";
        this.member = member;
    }

    /**
     * 연관관계 편의 메서드: 첨부파일 추가
     */
    public void addAttachment(BoardAttachment attachment) {
        this.boardAttachments.add(attachment);
        attachment.addBoard(this);
    }


    public void update(BoardRequest request){
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void changeStatus(){
        if ("Y".equals(this.status))
            this.status = "N";
        else
            this.status = "Y";
    }
}
