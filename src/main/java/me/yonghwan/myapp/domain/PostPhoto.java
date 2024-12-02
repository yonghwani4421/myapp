package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_photo_id")
    private Long id;

    private String photoName;
    private Long photoSize;
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostPhoto(String photoName, Long photoSize, String photoUrl) {
        this.photoName = photoName;
        this.photoSize = photoSize;
        this.photoUrl = photoUrl;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * 명시적인 행위
     */
    public void deletePost() {
        this.post = null;
    }
}
