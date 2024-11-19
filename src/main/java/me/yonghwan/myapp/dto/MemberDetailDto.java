package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Address;
import me.yonghwan.myapp.domain.Keyword;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberDetailDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNum;
    private String nickName;
    private Address address;
    private Role role;

    private long manner;
    private List<String> keywords;

    public MemberDetailDto(Long id, String email, String name, String phoneNum, String nickName, Address address, Role role, long manner, List<String> keywords) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.address = address;
        this.role = role;
        this.manner = manner;
        this.keywords = keywords;
    }

    public static MemberDetailDto from (Member member){

        List<String> keywordContents = member.getKeyWords().stream()
                .map(Keyword::getContent)
                .collect(Collectors.toList());
        return new MemberDetailDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhoneNum(),
                member.getNickName(),
                member.getAddress(),
                member.getRole(),
                member.calculateMannerScore(),
                keywordContents
        );
    }
}
