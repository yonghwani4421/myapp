package me.yonghwan.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Post;

@Data
@AllArgsConstructor
public class PostSaveRequest {

    private String title;
    private String content;
    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;

    public Post toEntity(Member member){
        return new Post(title,content,tradeType,price,placeName,latitude,longitude,member);
    }
}
