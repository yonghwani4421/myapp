package me.yonghwan.myapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNum;
    private String nickName;
    private Role role;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.phoneNum = member.getPhoneNum();
        this.nickName = member.getNickName();
        this.role = member.getRole();
    }
}
