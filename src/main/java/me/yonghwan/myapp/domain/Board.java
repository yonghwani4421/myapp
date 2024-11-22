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
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    @Lob
    private String content;
    private String status;

    /**
     * BoardAttachment
     * 1 : N 관계
     */
    @OneToMany(mappedBy = "board")
    private List<BoardAttachment> boardAttachments = new ArrayList<>();


    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 연관관계 편의 메서드: 첨부파일 추가
     */
    public void addAttachment(BoardAttachment attachment) {
        this.boardAttachments.add(attachment);
        attachment.addBoard(this);
    }





}
