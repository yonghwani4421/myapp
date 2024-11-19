package me.yonghwan.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.domain.Keyword;
import me.yonghwan.myapp.domain.Manner;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberUpdateRequest;
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



}