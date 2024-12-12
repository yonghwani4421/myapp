package me.yonghwan.myapp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.domain.Post;
import me.yonghwan.myapp.domain.PostLikes;
import me.yonghwan.myapp.domain.PostPhoto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;

    private List<PostPhotoResponse> photoList;
    private Long likesCount;

    public void createPostDetailResponse(Post post, Long likesCount){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tradeType = post.getTradeType();
        this.price = post.getPrice();
        this.placeName = post.getPlaceName();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        if (post.getPhotos() != null){
            List<PostPhotoResponse> list = new ArrayList<>();
            for (PostPhoto photo : post.getPhotos()) {
                list.add(PostPhotoResponse.builder().postPhotoId(photo.getId())
                        .photoName(photo.getPhotoName())
                        .photoUrl(photo.getPhotoUrl())
                        .photoSize(photo.getPhotoSize()).build());
            }
            this.photoList = list;
        } else {
            this.photoList = null;
        }

        this.likesCount = likesCount;
    }

    @Builder
    public PostDetailResponse(Long postId, String title, String content, String tradeType, Double price, String placeName, Double latitude, Double longitude, List<PostPhotoResponse> photoList, Long likesCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.tradeType = tradeType;
        this.price = price;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoList = photoList;
        this.likesCount = likesCount;
    }
}
