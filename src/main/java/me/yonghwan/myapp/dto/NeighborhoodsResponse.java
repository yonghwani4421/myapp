package me.yonghwan.myapp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NeighborhoodsResponse {
    private Long id;
    private String placeName;
    private String city;
    private String zipCode;
    private Double latitude;
    private Double longitude;


    @Builder
    public NeighborhoodsResponse(Long id, String placeName, String city, String zipCode, Double latitude, Double longitude) {
        this.id = id;
        this.placeName = placeName;
        this.city = city;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
