package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.BoardAttachment;

@Data
public class BoardAttachmentResponse {
    private String attachmentName;
    private String attachmentType;
    private Long attachmentSize;
    private String attachmentUrl;

    public BoardAttachmentResponse(String attachmentName, String attachmentType, Long attachmentSize, String attachmentUrl) {
        this.attachmentName = attachmentName;
        this.attachmentType = attachmentType;
        this.attachmentSize = attachmentSize;
        this.attachmentUrl = attachmentUrl;
    }

    public BoardAttachmentResponse(BoardAttachment boardAttachment) {
        this.attachmentName = boardAttachment.getAttachmentName();
        this.attachmentType = boardAttachment.getAttachmentType();
        this.attachmentSize = boardAttachment.getAttachmentSize();
        this.attachmentUrl = boardAttachment.getAttachmentUrl();
    }


}
