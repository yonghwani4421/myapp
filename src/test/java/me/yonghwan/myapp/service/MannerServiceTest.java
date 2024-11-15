package me.yonghwan.myapp.service;

import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.domain.Manner;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.repository.MemberRepository;
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




    @Test
    @DisplayName("save : 별점을 준 사람 받은사람 확인 / not null 체크 / 정확한 점수인지")
    public void save() throws Exception{
        // given
        Member m1 = memberRepository.save(
                Member.builder()
                        .email("test1@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123"))
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
                        .password(bCryptPasswordEncoder.encode("123"))
                        .name("kim2")
                        .phoneNum("01080754421")
                        .nickName("곱창국수2")
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );


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
        Member m1 = memberRepository.save(
                Member.builder()
                        .email("test1@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123"))
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
                        .password(bCryptPasswordEncoder.encode("123"))
                        .name("kim2")
                        .phoneNum("01080754421")
                        .nickName("곱창국수2")
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );
        Member m3 = memberRepository.save(
                Member.builder()
                        .email("test3@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123"))
                        .name("kim3")
                        .phoneNum("01080754421")
                        .nickName("곱창국수3")
                        .address("청송로")
                        .addressDetail("310동206호")
                        .zipCode("10101010")
                        .role(Role.ADMIN).build()
        );
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