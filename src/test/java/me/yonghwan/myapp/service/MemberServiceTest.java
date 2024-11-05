package me.yonghwan.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberUpdateRequest;
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
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetUp(){
        MockMvcBuilders.webAppContextSetup(context).build();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("[회원 등록][POST] => /api/members")
    public void saveMember() throws Exception{
        // given
        final String url = "/api/members";
        String email = "test@gmail.com";
        String password = "1234";
        String name = "test";
        String phoneNum = "01080754421";
        String nickName = "nick";
        String address = "서울시";
        String addressDetail = "영등포구";
        String zipCode = "1111";
        Role role = Role.USER;


        MemberRequest memberRequest = MemberRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNum(phoneNum)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .role(role)
                .build();

        String requestBody = objectMapper.writeValueAsString(memberRequest);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));


        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        List<Member> members = memberRepository.findAll();

        Assertions.assertThat(members.size()).isEqualTo(1);
        Assertions.assertThat(members.get(0).getEmail()).isEqualTo(email);
        Assertions.assertThat(members.get(0).getNickName()).isEqualTo(nickName);
        Assertions.assertThat(members.get(0).getRole()).isEqualTo(role);

    }

    @Test
    @DisplayName("[회원 전체 조회][GET] => /api/members")
    public void findAllMembers() throws Exception{
        // given
        final String url = "/api/members";
        String email = "test@gmail.com";
        String password = "1234";
        String name = "test";
        String phoneNum = "01080754421";
        String nickName = "nick";
        String address = "서울시";
        String addressDetail = "영등포구";
        String zipCode = "1111";
        Role role = Role.USER;

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNum(phoneNum)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .role(role)
                .build();

        memberRepository.save(member);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON));


        // then

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].name").value(name));
    }

    @Test
    @DisplayName("[회원 조회][GET] => /api/members/{id}")
    public void findMembers() throws Exception{
        // given
        final String url = "/api/members/";
        String email = "test@gmail.com";
        String password = "1234";
        String name = "test";
        String phoneNum = "01080754421";
        String nickName = "nick";
        String address = "서울시";
        String addressDetail = "영등포구";
        String zipCode = "1111";
        Role role = Role.USER;

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNum(phoneNum)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .role(role)
                .build();

        memberRepository.save(member);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url.concat(String.valueOf(member.getId())))
                .accept(MediaType.APPLICATION_JSON));


        // then

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(member.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(name));
    }

    @Test
    @DisplayName("[회원 수정][PUT] => /api/members/{id}")
    public void updateMember() throws Exception{
        // given
        final String url = "/api/members/";
        String email = "test@gmail.com";
        String password = "1234";
        String name = "test";
        String phoneNum = "01080754421";
        String nickName = "nick";
        String address = "서울시";
        String addressDetail = "영등포구";
        String zipCode = "1111";
        Role role = Role.USER;

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNum(phoneNum)
                .nickName(nickName)
                .address(address)
                .addressDetail(addressDetail)
                .zipCode(zipCode)
                .role(role)
                .build();

        memberRepository.save(member);


        String changeName = "kim";
        String chnageAddressDetail = "성동구";

        MemberUpdateRequest memberUpdateRequest = MemberUpdateRequest.builder()
                .name(changeName)
                .nickName(nickName)
                .address(address)
                .addressDetail(chnageAddressDetail)
                .zipCode(zipCode)
                .phoneNum(phoneNum)
                .build();

        String requestBody = objectMapper.writeValueAsString(memberUpdateRequest);


        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(url.concat(String.valueOf(member.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));


        // then

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(changeName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.address.addressDetail").value(chnageAddressDetail));
    }



}