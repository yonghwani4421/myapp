package me.yonghwan.myapp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostPhotoResponse {
    private Long postPhotoId;
    private String photoName;
    private Long photoSize;
    private String photoUrl;


    @Builder
    public PostPhotoResponse(Long postPhotoId, String photoName, Long photoSize, String photoUrl) {
        this.postPhotoId = postPhotoId;
        this.photoName = photoName;
        this.photoSize = photoSize;
        this.photoUrl = photoUrl;
    }
}
