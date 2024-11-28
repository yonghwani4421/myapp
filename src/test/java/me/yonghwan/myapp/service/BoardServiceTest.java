package me.yonghwan.myapp.service;

import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.aws.config.AwsS3Properties;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.*;
import me.yonghwan.myapp.dto.BoardRequest;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.BoardLikesRepository;
import me.yonghwan.myapp.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @DisplayName("게시물 등록 테스트")
    public void save() throws Exception{
        // given
        Board board =  new Board("title1","content1",m1);

        // when
        Board save = boardService.save(board);

        // then
        assertThat(save).isEqualTo(board);
    }

    @Test
    @DisplayName("게시물을 저장한다.")
    public void saveBoard() throws Exception{

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        Board board = boardService.saveBoardWithAttachments(new Board("title1", "content1",m1)
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        assertNotNull(board.getBoardAttachments().get(0).getAttachmentUrl(), "업로드된 파일 URL이 null이어서는 안 됩니다.");
        assertTrue(board.getBoardAttachments().get(0).getAttachmentUrl().startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
        assertTrue(board.getBoardAttachments().get(0).getAttachmentUrl().contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(board.getBoardAttachments().get(0).getAttachmentType(),"txt", "URL에 버킷 이름이 포함되어야 합니다.");
        assertEquals(board.getBoardAttachments().size(),2, "갯수가 2개 이상이여야 합니다.");
        assertEquals(board.getTitle(),"title1", "제목이 title1으로 정상적으로 들어가야합니다.");
    }


    @Test
    @DisplayName("저장한 게시물의 제목과 내용 첨부파일을 수정한다.")
    public void updateBoard() throws Exception{

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        Board board = boardService.saveBoardWithAttachments(new Board("title1", "content1",m1)
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        Long id = board.getBoardAttachments().get(0).getId();
        boardService.updateBoard(new BoardRequest("title2","content2",Arrays.asList(id))
                ,Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)),board.getId());

        em.flush();
        em.clear();

        Board findBoard = boardService.findById(board.getId());

        assertEquals(findBoard.getTitle(),"title2", "제목이 title2으로 정상적으로 들어가야합니다.");
        assertEquals(findBoard.getBoardAttachments().size(),3,"파일의 갯수가 맞아야합니다.");
    }
    @Test
    @DisplayName("게시물을 삭제한다.")
    @Transactional
    public void deleteBoard() throws Exception{

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        Board board = boardService.saveBoardWithAttachments(new Board("title1", "content1",m1)
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));


        Long findId = board.getId();

        boardService.deleteBoardById(findId);

        em.flush();
        em.clear();


        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Board findBoard = boardService.findById(findId);
        });

        assertEquals(ex.getMessage(),"not found : "+findId);
    }


    @Test
    @DisplayName("첨부파일 1개만 삭제한다.")
    public void deleteFile() throws Exception{
        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        // 게시물 등록 첨부파일 s3에 저장
        Board board = boardService.saveBoardWithAttachments(new Board("title1", "content1",m1)
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
        Board board = boardService.saveBoardWithAttachments(new Board("title1", "content1",m1)
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
        Board board =  new Board("title1","content1",m1);

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.saveBoardWithAttachments(board
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
        Board board =  new Board("title1","content1",m1);

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.saveBoardWithAttachments(board
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
        Board board =  new Board("title1","content1",m1);

        File file = new File(PATH);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");
        Board saveBoard = boardService.saveBoardWithAttachments(board
                , Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file)));

        boardService.addOrCancelLikes(saveBoard.getId(),m1);

        assertEquals(boardLikesRepository.findAll().size(),1,"숫자가 정상적으로 1개여야합니다.");

        boardService.addOrCancelLikes(saveBoard.getId(),m1);

        assertEquals(boardLikesRepository.findAll().size(),0,"숫자가 정상적으로 0개여야합니다.");

        // then
    }

    @Test
    @DisplayName("게시물 리스트 페이징 처리 조회")
    public void getPageList() throws Exception{
        // given
        for (int i = 0; i < 20; i++) {
            boardService.saveBoardWithAttachments(new Board("title"+i,"content"+i,m1),null);
        }
        // then
        assertEquals(boardService.getBoardList(PageRequest.of(0,10)).getSize(),10);
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