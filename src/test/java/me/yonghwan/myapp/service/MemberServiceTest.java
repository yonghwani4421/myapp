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




}