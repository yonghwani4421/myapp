package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        LoginMember loginMember = new LoginMember();
        loginMember.setEmail(member.getEmail());
        loginMember.setPassword(member.getPassword());
        loginMember.setRole(member.getRole());

        if (member != null){
            return new CustomMemberDetails(loginMember);
        }
        return null;
    }
}
