package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(MemberRequest joinMemberDTO){
        Member member = Member.builder().memberDTO(joinMemberDTO).build();
        return memberRepository.save(member);
    }
}
