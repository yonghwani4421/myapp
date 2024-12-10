package me.yonghwan.myapp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchRequest {
    private String title;
    private String placeName;
    private String tradeType;

    private Double priceGoe;
    private Double priceLoe;

    @Builder
    public PostSearchRequest(String title, String placeName, String tradeType, Double priceGoe, Double priceLoe) {
        this.title = title;
        this.placeName = placeName;
        this.tradeType = tradeType;
        this.priceGoe = priceGoe;
        this.priceLoe = priceLoe;
    }
}
