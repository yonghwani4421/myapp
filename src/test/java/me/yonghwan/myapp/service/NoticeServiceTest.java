package me.yonghwan.myapp.service;

import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class NoticeServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NoticeService noticeService;


    @Test
    @DisplayName("save : 공지사항 생성 " +
            "1. 저장후 값 확인")
    public void NoticeServiceTest() throws Exception{
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

        String title = "title1";
        String content = "content1";

        // when
        Notice notice = noticeService.save(Notice.builder()
                .title(title)
                .content(content).member(m1).build());

        Notice findNotice = noticeService.findById(notice.getId());

        // then
        assertEquals(findNotice.getTitle(),title);
        assertEquals(findNotice.getContent(),content);
    }

    @Test
    @DisplayName("list : 리스트 조회 " +
            "1. 갯수확인" +
            "2. 값확인")
    public void getList() throws Exception{
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

        for (int i = 0; i < 10; i++) {
            Notice notice = noticeService.save(Notice.builder()
                    .title("title" + i)
                    .content("content" + i).member(m1).build());
        }

        // when

        List<Notice> list = noticeService.findAll();


        // then


        assertEquals(list.size(), 10);
        assertEquals(list.get(1).getTitle(),"title1");
    }

    @Test
    @DisplayName("delete : 공지사항 삭제" +
            "1. 삭제후 갯수 확인")
    public void deleteNotice() throws Exception{
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

        Notice notice = noticeService.save(Notice.builder()
                    .title("title")
                    .content("content").member(m1).build());

        // when

        noticeService.delete(notice.getId());

        List<Notice> list = noticeService.findAll();


        // then
        assertEquals(list.size(),0);
    }

    @Test
    @DisplayName("update : 공지사항 수정" +
            "1. 값 정상 수정확인")
    public void updateNotice() throws Exception{
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

        String title = "title1";
        String content = "content1";

        // when
        Notice notice = noticeService.save(Notice.builder()
                .title(title)
                .content(content).member(m1).build());



        String updateContent = "content2";
        Notice updateNotice = noticeService.update(notice.getId(), new NoticeDto(title, updateContent));


        // then
        assertEquals(updateNotice.getContent(),updateContent);
    }



}