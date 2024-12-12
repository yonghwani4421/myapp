package me.yonghwan.myapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;
    @Builder
    public PostResponse(Long id, String title, String content, String tradeType, Double price, String placeName, Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tradeType = tradeType;
        this.price = price;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
