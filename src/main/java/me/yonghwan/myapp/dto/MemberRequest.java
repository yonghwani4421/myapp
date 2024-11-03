package me.yonghwan.myapp.dto;

import lombok.Getter;
import me.yonghwan.myapp.domain.Role;

@Getter
public class MemberRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private Role role;
}
