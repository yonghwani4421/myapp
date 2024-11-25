package me.yonghwan.myapp.service;

import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.aws.config.AwsS3Properties;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.*;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.BoardLikesRepository;
import me.yonghwan.myapp.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    S3Service s3Service;

    @Autowired
    BoardLikesRepository boardLikesRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AwsS3Properties awsS3Properties;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    EntityManager em;

    private static final String PATH = "C:\\workspace\\test.txt";


    @Test
    @DisplayName("게시물 등록 테스트")
    public void save() throws Exception{
        // given
        Board board =  new Board("title1","content1");

        LoginMember member = new LoginMember();
        member.setEmail("test1@gmail.com");
        member.setPassword("temppassword");
        member.setRole(Role.valueOf("ADMIN"));

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // when
        Board save = boardService.save(board);

        // then
        assertThat(save).isEqualTo(board);
    }

    @Test
    @DisplayName("s3에 파일들을 업로드하고 DB에 저장한다.")
    public void uploadFileAndSave() throws Exception{

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        Board board = boardService.CreateBoardWithAttachmentSave(new Board("title1", "content1")
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        assertNotNull(board.getBoardAttachments().get(0).getAttachmentUrl(), "업로드된 파일 URL이 null이어서는 안 됩니다.");
        assertTrue(board.getBoardAttachments().get(0).getAttachmentUrl().startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
        assertTrue(board.getBoardAttachments().get(0).getAttachmentUrl().contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(board.getBoardAttachments().get(0).getAttachmentType(),"txt", "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(board.getBoardAttachments().size(),2, "갯수가 2개 이상이여야 합니다.");
        assertEquals(board.getTitle(),"title1", "제목이 title1으로 정상적으로 들어가야합니다.");
    }


    @Test
    @DisplayName("첨부파일 1개만 삭제한다.")
    public void deleteFile() throws Exception{
        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        // 게시물 등록 첨부파일 s3에 저장
        Board board = boardService.CreateBoardWithAttachmentSave(new Board("title1", "content1")
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        BoardAttachment boardAttachment = board.getBoardAttachments().get(0);
        s3Service.deleteFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl()));

        assertFalse(s3Service.isExistFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl())),"파일삭제가 되어야합니다.");

        boardService.deleteAttachment(boardAttachment.getId());

        em.flush();
        em.clear();

        Board findBoard = boardService.findByIdWithAttachments(board.getId());

        // then
        assertEquals(findBoard.getBoardAttachments().size(),1,"DB에서 정상적으로 삭제가 되어야 합니다.");
    }

    @Test
    @DisplayName("첨부파일 전부 삭제한다.")
    public void deleteAllFile() throws Exception{
        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        // 게시물 등록 첨부파일 s3에 저장
        Board board = boardService.CreateBoardWithAttachmentSave(new Board("title1", "content1")
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        board.getBoardAttachments().stream().forEach(boardAttachment -> {
            s3Service.deleteFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl()));
            assertFalse(s3Service.isExistFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl())),"파일삭제가 되어야합니다.");
        });
        // 전체 삭제
        board.getBoardAttachments().stream().forEach(boardAttachment -> boardService.deleteAttachment(boardAttachment.getId()));

        em.flush();
        em.clear();

        Board findBoard = boardService.findByIdWithAttachments(board.getId());

        // then
        assertEquals(findBoard.getBoardAttachments().size(),0,"DB에서 정상적으로 삭제가 되어야 합니다.");
    }


    @Test
    @DisplayName("게시물 등록 ( 게시물 + 첨부파일 )")
    public void boardSaveWithAttachment() throws Exception{
        // given
        Board board =  new Board("title1","content1");

        LoginMember member = new LoginMember();
        member.setEmail("test1@gmail.com");
        member.setPassword("temppassword");
        member.setRole(Role.valueOf("ADMIN"));

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.CreateBoardWithAttachmentSave(board
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        // when

        assertNotNull(saveBoard.getBoardAttachments().get(0).getAttachmentUrl(), "업로드된 파일 URL이 null이어서는 안 됩니다.");
        assertTrue(saveBoard.getBoardAttachments().get(0).getAttachmentUrl().startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
        assertTrue(saveBoard.getBoardAttachments().get(0).getAttachmentUrl().contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(saveBoard.getBoardAttachments().get(0).getAttachmentType(),"txt", "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(saveBoard.getBoardAttachments().size(),2, "갯수가 2개 이상이여야 합니다.");
        assertEquals(saveBoard.getTitle(),"title1", "제목이 title1으로 정상적으로 들어가야합니다.");

        // then
    }


    @Test
    @DisplayName("게시물 등록 ( 게시물 + 첨부파일 ) 삭제")
    public void boardSaveWithAttachmentAndDeleteAll() throws Exception{
        // given
        Board board =  new Board("title1","content1");

        LoginMember member = new LoginMember();
        member.setEmail("test1@gmail.com");
        member.setPassword("temppassword");
        member.setRole(Role.valueOf("ADMIN"));

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.CreateBoardWithAttachmentSave(board
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));



        saveBoard.getBoardAttachments().stream().forEach(boardAttachment -> {
            s3Service.deleteFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl()));
            assertFalse(s3Service.isExistFile(fileUtil.convertToFileName(boardAttachment.getAttachmentUrl())),"파일삭제가 되어야합니다.");
        });
        boardService.deleteById(saveBoard.getId());


        List<Board> all = boardService.findAll();

        List<BoardAttachment> attachmentAll = boardService.findAttachmentAll();

        assertEquals(all.size(),0, "전부삭제가 되어야합니다.");
        assertEquals(attachmentAll.size(),0, "전부삭제가 되어야합니다.");

    }


    @Test
    @DisplayName("게시물에 좋아요 추가 / 좋아요 삭제")
    public void boardAddLike() throws Exception{
        // given
        Board board =  new Board("title1","content1");

        Member m1 = createMember();

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.CreateBoardWithAttachmentSave(board
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));



        assertTrue(boardService.addLikes(saveBoard.getId(), m1.getId()),"추가가 정상적으로 되어야합니다.");
        assertEquals(boardLikesRepository.findAll().size(),1,"숫자가 정상적으로 1개여야합니다.");
        assertTrue(boardService.cancelLikes(saveBoard.getId(), m1.getId()),"삭제가 정상적으로 되어야합니다.");
        assertEquals(boardLikesRepository.findAll().size(),0,"숫자가 정상적으로 0개여야합니다.");

        // then
    }



    @Test
    @DisplayName("게시물에 좋아요 삭제")
    public void boardLikeCancel() throws Exception{
        // given
        Board board =  new Board("title1","content1");

        Member m1 = createMember();

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.CreateBoardWithAttachmentSave(board
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));


        boardService.addLikes(saveBoard.getId(), m1.getId());

        boolean b1 = boardService.cancelLikes(saveBoard.getId(), m1.getId());

        assertTrue(b1,"삭제가 정상적으로 되어야합니다.");
        List<BoardLikes> all = boardLikesRepository.findAll();
        assertEquals(all.size(),0,"숫자가 정상적으로 0개여야합니다.");
        // then
    }

    @Test
    @DisplayName("게시물 리스트 페이징해서 조회하기")
    public void getPageList() throws Exception{
        // given
        Member m1 = createMember();

        for (int i = 0; i < 20; i++) {
            boardService.CreateBoardWithAttachmentSave(new Board("title"+i,"content"+i),null);
        }
        // when




        // then
    }


    private Member createMember() {
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

        LoginMember member = new LoginMember();
        member.setEmail(m1.getEmail());
        member.setPassword("temppassword");
        member.setRole(m1.getRole());

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return m1;
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