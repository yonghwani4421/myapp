package me.yonghwan.myapp.dto;

import lombok.Builder;
import lombok.Data;
import me.yonghwan.myapp.domain.Post;

@Data
public class PostTradeResponse {
    private Long postId;
    private Long buyerId;
    private String title;
    private String content;
    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;

    public PostTradeResponse() {
    }

    public static PostTradeResponse createResponse(Post post, Long buyerId){
       return new PostTradeResponse(post.getId(), buyerId, post.getTitle(), post.getContent(), post.getTradeType(), post.getPrice(), post.getPlaceName(), post.getLatitude(), post.getLongitude());
    }
    @Builder
    public PostTradeResponse(Long postId, Long buyerId, String title, String content, String tradeType, Double price, String placeName, Double latitude, Double longitude) {
        this.postId = postId;
        this.buyerId = buyerId;
        this.title = title;
        this.content = content;
        this.tradeType = tradeType;
        this.price = price;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
