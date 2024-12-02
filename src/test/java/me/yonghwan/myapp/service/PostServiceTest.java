package me.yonghwan.myapp.service;

import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.aws.config.AwsS3Properties;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Post;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.dto.PostRequest;
import me.yonghwan.myapp.dto.PostSaveRequest;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.MemberRepository;
import me.yonghwan.myapp.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class PostServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    S3Service s3Service;
    @Autowired
    AwsS3Properties awsS3Properties;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    EntityManager em;
    private static final String PATH = "C:\\workspace\\test.txt";

    Member m1;

    @BeforeEach
    public void before(){
        memberRepository.deleteAll(); // 기존 데이터 초기화
        m1 = createMember("test1@gmail.com", "곱창국수1");
        LoginMember member = new LoginMember();
        member.setEmail(m1.getEmail());
        member.setPassword("temppassword");
        member.setRole(m1.getRole());

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
    @DisplayName("거래 게시물을 등록한다.")
    public void savePost() throws Exception{

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        PostSaveRequest request = new PostSaveRequest("title1", "content1", "A", 10000.0, "장기동", 1000.0, 1000.0);

        Post post = postService.savePostWithAttachment(request.toEntity(m1)
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        assertNotNull(post.getPhotos().get(0).getPhotoUrl(), "업로드된 파일 URL이 null이어서는 안 됩니다.");
        assertTrue(post.getPhotos().get(0).getPhotoUrl().startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
        assertTrue(post.getPhotos().get(0).getPhotoUrl().contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(post.getPhotos().size(),2, "갯수가 2개 이상이여야 합니다.");
        assertEquals(post.getTitle(),"title1", "제목이 title1으로 정상적으로 들어가야합니다.");
    }



    @Test
    @DisplayName("거래 게시물을 수정한다.")
    public void updatePost() throws Exception{
        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        PostSaveRequest request = new PostSaveRequest("title1", "content1", "A", 10000.0, "장기동", 1000.0, 1000.0);

        Post post = postService.savePostWithAttachment(request.toEntity(m1)
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        PostRequest updateRequest = new PostRequest("title2", "content2", "A", 20000.0, "장기동", 1000.0, 1000.0
                , Arrays.asList(post.getPhotos().get(0).getId()));

        // when
        Post updatePost = postService.updatePost(updateRequest, Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)), post.getId());


        // then

        assertEquals(updatePost.getTitle(),"title2", "제목이 title2으로 정상적으로 들어가야합니다.");
        assertEquals(updatePost.getPhotos().size(),3, "갯수가 3개 이상이여야 합니다.");
    }




    public static MultipartFile convertToMultipartFile(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(
                    file.getName(),                // MultipartFile의 이름
                    file.getName(),                // 원본 파일 이름
                    "application/octet-stream",    // MIME 타입 (필요시 설정)
                    inputStream                    // 파일 데이터
            );
        }
    }

}