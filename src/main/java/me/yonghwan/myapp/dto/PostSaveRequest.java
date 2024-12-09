package me.yonghwan.myapp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveRequest {

    private String title;
    private String content;
    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .content(content)
                .title(title)
                .price(price)
                .tradeType(tradeType)
                .placeName(placeName)
                .latitude(latitude)
                .longitude(longitude).build();
    }
}
