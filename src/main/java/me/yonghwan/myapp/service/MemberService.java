package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberUpdateRequest;
import me.yonghwan.myapp.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 등록
     * @param request
     * @return
     */
    @Transactional
    public Member save(MemberRequest request){
        return memberRepository.save(request.toEntity(bCryptPasswordEncoder));
    }

    /**
     * 회원 전체 조회
     * @return
     */
    public List<Member> findMembers(){
       return memberRepository.findAll();
    }

    /**
     * 회원 Id로 조회
     * @param id
     * @return
     */
    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: " + id));
    }
    public Member findMemberWithKeyWords(Long id){
        return memberRepository.findMemberWithKeyWords(id).orElseThrow(()-> new IllegalArgumentException("not found: " + id));
    }
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("not found : " + email));
    }
    /**
     * 회원 수정
     * @param request
     * @param id
     */
    @Transactional
    public void update(MemberUpdateRequest request, Long id){

        Member member = memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+ id));

        // 변경 감지
        member.update(request.getName()
                , request.getPhoneNum()
                , request.getNickName()
                , request.getAddress()
                , request.getAddressDetail()
                , request.getZipCode());
    }

}
