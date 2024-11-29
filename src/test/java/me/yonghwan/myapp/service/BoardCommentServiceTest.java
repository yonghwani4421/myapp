package me.yonghwan.myapp.service;

import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardComment;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.BoardCommentRequest;
import me.yonghwan.myapp.dto.BoardCommentResponse;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.repository.BoardRepository;
import me.yonghwan.myapp.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class BoardCommentServiceTest {

    @Autowired
    BoardCommentService boardCommentService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    Member member;
    Board board;


    @BeforeEach
    public void before(){
        memberRepository.deleteAll(); // 기존 데이터 초기화
        member = createMember("test1@gmail.com", "곱창국수1");
        board = createBoard("title1","content1");
        setAuthenticationInContext();


    }

    private Board createBoard(String title, String content) {
       return boardRepository.save(new Board(title,content, member));
    }

    /**
     * 사용자 인증정보 설정
     */
    private void setAuthenticationInContext() {
        LoginMember member = new LoginMember();
        member.setEmail(this.member.getEmail());
        member.setPassword("temppassword");
        member.setRole(this.member.getRole());

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * 유저 생성
     * @param email
     * @param nickName
     * @return
     */

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
    @DisplayName("댓글 작성")
    public void createComment() throws Exception{
        // given
        String content = "댓글";
        BoardCommentRequest request = new BoardCommentRequest(null, content);

        boardCommentService.createComment(request,board.getId());
        boardCommentService.createComment(request,board.getId());
        boardCommentService.createComment(request,board.getId());
        // when
        List<BoardComment> flatList = boardCommentService.getCommentFlatList(board.getId());

        for (BoardComment boardComment : flatList) {
            System.out.println("boardComment = " + boardComment.getContent());
        }

        // then
        assertEquals(flatList.get(0).getContent(),content);
    }


    @Test
    @DisplayName("대댓글 작성")
    public void createCommentAndComment() throws Exception{
        // given
        String content = "댓글";
        BoardCommentRequest comment1 = new BoardCommentRequest(null, content);
        BoardCommentRequest comment2 = new BoardCommentRequest(null, content);
        BoardComment boardComment = boardCommentService.createComment(comment1, board.getId());
        BoardCommentRequest comment3 = new BoardCommentRequest(boardComment.getId(), content);
        boardCommentService.createComment(comment3,board.getId());

        // when

        List<BoardCommentResponse> boardCommentList = boardCommentService.getCommentsByBoardId(board.getId());

        printComment(boardCommentList);

        // then
        assertFalse(boardCommentList.get(0).getChildComments().isEmpty(),"계층 하위에 있다.");
        assertEquals(boardCommentList.get(0).getContent(),boardCommentList.get(0).getChildComments().get(0).getContent(),"두 계층은 댓글 내용이 같아야한다.");
    }

    private static void printComment(List<BoardCommentResponse> boardCommentList) {
        for (BoardCommentResponse boardCommentResponse : boardCommentList) {
            System.out.println("content = " + boardCommentResponse.getContent());
            if (boardCommentResponse.getChildComments() != null && !boardCommentResponse.getChildComments().isEmpty()){
                System.out.println("child comments: ");
                printComment(boardCommentResponse.getChildComments());
            }
        }
    }

}