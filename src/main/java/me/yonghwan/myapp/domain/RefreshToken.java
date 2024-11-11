package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String refreshToken;


    @Builder
    public RefreshToken(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }
}
