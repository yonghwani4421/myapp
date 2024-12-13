package me.yonghwan.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.domain.*;
import me.yonghwan.myapp.dto.*;
import me.yonghwan.myapp.repository.KeyWordRepository;
import me.yonghwan.myapp.repository.MannerRepository;
import me.yonghwan.myapp.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    KeyWordRepository keyWordRepository;

    @Autowired
    MannerRepository mannerRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    Member member;

    @BeforeEach
    public void before(){
        memberRepository.deleteAll(); // 기존 데이터 초기화
        member = createMember("test1@gmail.com", "곱창국수1");
        LoginMember member = new LoginMember();
        member.setEmail(member.getEmail());
        member.setPassword("temppassword");
        member.setRole(member.getRole());

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private Member createMember(String email, String nickName) {
        return memberRepository.save(
                Member.builder()
                        .email(email)
                        .password("123")
                        .name("testName")
                        .phoneNum("01080754421")
                        .nickName(nickName)
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );
    }



    @Test
    @DisplayName("회원 디테일 조회")
    public void getMember() throws Exception{
        // given
        Member m1 = memberRepository.save(
                Member.builder()
                        .email("test1@gmail.com")
                        .password("123")
                        .name("kim1")
                        .phoneNum("01080754421")
                        .nickName("곱창국수1")
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );
        Member m2 = memberRepository.save(
                Member.builder()
                        .email("test2@gmail.com")
                        .password("123")
                        .name("kim2")
                        .phoneNum("01080754421")
                        .nickName("곱창국수2")
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );

        String content1 = "당근1";
        String content2 = "당근2";
        keyWordRepository.save(
                Keyword.builder().content(content1).member(m1).build()
        );

        keyWordRepository.save(
                Keyword.builder().content(content2).member(m1).build()
        );

        mannerRepository.save(Manner.builder().giver(m2).score(5).receiver(m1).build());


        em.flush();
        em.clear();


        Member memberWithKeyWords = memberService.findMemberWithKeyWords(m1.getId());


        System.out.println("memberWithKeyWords = " + memberWithKeyWords.calculateMannerScore());

        List<Keyword> keyWords = memberWithKeyWords.getKeyWords();
        for (Keyword keyWord : keyWords) {
            System.out.println("keyWord = " + keyWord.getContent());
        }


        // when

        Assertions.assertThat(keyWords.size()).isEqualTo(2);
        Assertions.assertThat(memberWithKeyWords.calculateMannerScore()).isEqualTo(5);
        // then
    }


    @Test
    @DisplayName("나의 동네를 등록한다.")
    public void saveNeighborhood() throws Exception{
        // given

        NeighborhoodsRequest request = new NeighborhoodsRequest("장기동", "김포시", "10000", 37.64417039999994, 126.66492555393278);

        memberService.saveNeighborhoods(request, member);

        // when
        em.flush();
        em.clear();

        Member findMembers = memberService.findMembers().get(0);

        // then
        Assertions.assertThat(findMembers.getNeighborhoodsList().get(0).getCity()).isEqualTo("김포시");
        Assertions.assertThat(findMembers.getNeighborhoodsList().get(0).getPlaceName()).isEqualTo("장기동");
    }




}