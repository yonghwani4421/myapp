package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAttachment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_attachment_id")
    private Long id;

    /**
     * Board
     * N : 1 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String attachmentName;
    private String attachmentType;
    private Long attachmentSize;
    private String attachmentUrl;


    public BoardAttachment(String attachmentName, String attachmentType, Long attachmentSize, String attachmentUrl) {
        this.attachmentName = attachmentName;
        this.attachmentType = attachmentType;
        this.attachmentSize = attachmentSize;
        this.attachmentUrl = attachmentUrl;
    }

    public void addBoard(Board board){
        this.board = board;
    }
    public void deleteBoard(){
        this.board = null;
    }

}
