package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    @Column(unique = true)
    private String nickName;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * keyword 양방향 관계
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keyWords = new ArrayList<>();

    /**
     * Manner 양방향 관계
     */
    @OneToMany(mappedBy = "giver", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Manner> givers = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Manner> receiver = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards  = new ArrayList<>();
    /**
     * Post 양방향
     */
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<Trade> trades  = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Neighborhoods> neighborhoodsList  = new ArrayList<>();


    /**
     * 나의 매너 점수를 계산해주는 함수
     * @return
     */
    public long calculateMannerScore(){
        if (this.receiver != null && !this.receiver.isEmpty()){
            return this.receiver.stream().mapToInt(Manner::getScore).sum() / this.receiver.size();
        } else {
            return 0L;
        }
    }



    public void update(String name, String phoneNum, String nickName, String address, String addressDetail, String zipCode){
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.address = Address.builder().address(address).addressDetail(addressDetail).zipCode(zipCode).build();
    }

    @Builder
    public Member(String email, String password, String name, String phoneNum, String nickName, String address, String addressDetail, String zipCode, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.address = Address.builder().address(address).addressDetail(addressDetail).zipCode(zipCode).build();
        this.role = role;
    }


}
