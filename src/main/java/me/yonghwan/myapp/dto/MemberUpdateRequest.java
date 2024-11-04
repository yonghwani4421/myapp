package me.yonghwan.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdateRequest {
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

}
