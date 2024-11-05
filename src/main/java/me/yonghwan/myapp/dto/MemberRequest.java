package me.yonghwan.myapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import me.yonghwan.myapp.common.annotation.ValidEnum;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;

@Data
public class MemberRequest {
    @Email @NotBlank(message = "이메일을 입력하세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    @NotBlank(message = "전화번호를 입력하세요.")
    private String phoneNum;
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickName;
    @NotBlank(message = "주소를 입력하세요.")
    private String address;
    @NotBlank(message = "상세주소를 입력하세요.")
    private String addressDetail;
    @NotBlank(message = "우편번호를 입력하세요.")
    private String zipCode;
    @ValidEnum(enumClass = Role.class, message = "유효하지 않습니다. [USER,ADMIN]")
    private Role role;

    @Builder
    public MemberRequest(String email, String password, String name, String phoneNum, String nickName, String address, String addressDetail, String zipCode, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
        this.role = role;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNum(phoneNum)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .role(role).build();
    }


}
