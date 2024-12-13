package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Neighborhoods;

@Data
public class NeighborhoodsRequest {
    private String placeName;
    private String city;
    private String zipCode;
    private Double latitude;
    private Double longitude;


    public Neighborhoods toEntity(Member member){
        return Neighborhoods.builder()
                .placeName(placeName)
                .city(city)
                .zipCode(zipCode)
                .latitude(latitude)
                .longitude(longitude)
                .member(member)
                .build();
    }

    public NeighborhoodsRequest(String placeName, String city, String zipCode, Double latitude, Double longitude) {
        this.placeName = placeName;
        this.city = city;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
