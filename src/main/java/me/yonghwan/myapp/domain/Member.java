package me.yonghwan.myapp.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import me.yonghwan.myapp.dto.MemberRequest;

import java.time.LocalDateTime;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Verified verified;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public Member(MemberRequest memberDTO) {
        this.email = memberDTO.getEmail();
        this.password = memberDTO.getPassword();
        this.name = memberDTO.getName();
        this.phoneNum = memberDTO.getPhoneNum();
        this.nickName = memberDTO.getNickName();
        this.address = memberDTO.getAddress();
        this.addressDetail = memberDTO.getAddressDetail();
        this.zipCode = memberDTO.getZipCode();
        this.role = memberDTO.getRole();
    }
}
