package me.yonghwan.myapp.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Role;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.dto.LoginMember;
import me.yonghwan.myapp.repository.RefreshTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);

            // 조건이 해당되면 메소드 종료
            return;
        }
        String token = authorization.split(" ")[1];
        // token 만료 일자 확인
        if (jwtUtil.isExpired(token)){
            log.info("## token expired!!");
            filterChain.doFilter(request,response);
            // 조건이 해당되면 메소드 종료
            return;
        }

        String email = jwtUtil.getMemberEmail(token);
        String role = jwtUtil.getRole(token);


        // 아래코드 작성

        LoginMember member = new LoginMember();
        member.setEmail(email);
        member.setPassword("temppassword");
        member.setRole(Role.valueOf(role));
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);


    }
}
