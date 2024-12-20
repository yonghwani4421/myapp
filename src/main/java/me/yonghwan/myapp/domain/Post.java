package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.dto.PostDetailResponse;
import me.yonghwan.myapp.dto.PostPhotoResponse;
import me.yonghwan.myapp.dto.PostRequest;
import me.yonghwan.myapp.dto.PostResponse;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private String tradeType;
    private Double price;
    private String placeName;
    private Double latitude;
    private Double longitude;


    /**
     * Member 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<PostPhoto> photos = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<PostLikes> postLikes = new ArrayList<>();

    @Builder
    public Post(String title, String content, String tradeType, Double price, String placeName, Double latitude, Double longitude, Member member) {
        this.title = title;
        this.content = content;
        this.tradeType = tradeType;
        this.price = price;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.member = member;
    }

    public PostResponse toResponse(){
        return PostResponse.builder()
                .id(id)
                .title(title)
                .content(content)
                .tradeType(tradeType)
                .placeName(placeName)
                .price(price)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }


    public PostDetailResponse toDetailResponse(Long likesCount){

        List<PostPhotoResponse> list = new ArrayList<>();
        if (photos!=null){
            photos.stream().forEach(photo -> {
                list.add(
                        PostPhotoResponse.builder()
                                .postPhotoId(photo.getId())
                                .photoName(photo.getPhotoName())
                                .photoSize(photo.getPhotoSize())
                                .photoUrl(photo.getPhotoUrl())
                                .build()
                );
            });
        }


        return PostDetailResponse.builder()
                .postId(id)
                .title(title)
                .content(content)
                .tradeType(tradeType)
                .placeName(placeName)
                .price(price)
                .latitude(latitude)
                .longitude(longitude)
                .photoList(list)
                .likesCount(likesCount)
                .build();
    }


    /**
     * 연관관계 편의 메서드: 첨부파일 추가
     */
    public void addAttachment(PostPhoto postPhoto) {
        this.photos.add(postPhoto);
        postPhoto.setPost(this);
    }


    public void update(PostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.tradeType = request.getTradeType();
        this.price = request.getPrice();
        this.placeName = request.getPlaceName();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
    }
}
