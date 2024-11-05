package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private Verified verified;


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
