package me.yonghwan.myapp.service;

import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.domain.Manner;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MannerServiceTest {

    @Autowired
    MannerService mannerService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EntityManager em;

    Member m1;
    Member m2;
    Member m3;

    @BeforeEach
    public void before(){
        memberRepository.deleteAll(); // 기존 데이터 초기화
        m1 = createMember("test1@gmail.com", "곱창국수1");
        m2 = createMember("test2@gmail.com", "곱창국수2");
        m3 = createMember("test3@gmail.com", "곱창국수3");
    }
    private Member createMember(String email, String nickName) {
        return memberRepository.save(
                Member.builder()
                        .email(email)
                        .password(bCryptPasswordEncoder.encode("123"))
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
    @DisplayName("save : 별점을 준 사람 받은사람 확인 / not null 체크 / 정확한 점수인지")
    public void save() throws Exception{
        // given



        // when
        Manner manner = mannerService.save(
                Manner.builder()
                        .giver(m1)
                        .receiver(m2)
                        .score(5).build());


        Manner findManner = mannerService.findById(manner.getId());
        // then

        assertNotNull(manner, "저장된 객체가 null일 수는 없다.");
        assertThat(manner).isEqualTo(findManner);
        assertThat(manner.getGiver()).isEqualTo(m1);
        assertThat(manner.getReceiver()).isEqualTo(m2);
        assertThat(manner.getScore()).isEqualTo(5);
    }

    @Test
    @DisplayName("list : 내가 받은 매너리스트 갯수 / 매너점수 평균")
    public void totalMannerList() throws Exception{
        // given

        // when
        mannerService.save(
                Manner.builder()
                        .giver(m1)
                        .receiver(m2)
                        .score(5).build());
        mannerService.save(
                Manner.builder()
                        .giver(m3)
                        .receiver(m2)
                        .score(3).build());

        em.flush();
        em.clear();

        Member member = memberRepository.findById(m2.getId()).get();
        List<Manner> receiver = member.getReceiver();
        // then

        assertEquals(receiver.size(),2);
        assertEquals(member.calculateMannerScore(),4);
    }
}