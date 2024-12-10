package me.yonghwan.myapp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostListResponse {
    private Long postId;
    private String title;
    private String tradeType;
    private Double price;
    private String placeName;
    private LocalDateTime createDate;

    @QueryProjection
    public PostListResponse(Long postId, String title, String tradeType, Double price, String placeName,LocalDateTime createDate) {
        this.postId = postId;
        this.title = title;
        this.tradeType = tradeType;
        this.price = price;
        this.placeName = placeName;
        this.createDate = createDate;
    }
}
