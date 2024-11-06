package me.yonghwan.myapp.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        // 이메일 비밀번호 검증
        String email = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("## Login email : {}",email);
        log.info("## Login password : {}",password);


        // 로그인 정보를 담는다
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);


        // authenticationManger에서 DB의 회원정보와 비교하여 검증을 진행함
        return authenticationManager.authenticate(authToken);
    }


    /**
     * 로그인 성공시
     * @param request
     * @param response
     * @param chain
     * @param authentication the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("## Login sucess");

        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        // email
        String email = customMemberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        // role
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(email, role, Duration.ofMinutes(3).toMillis());

        response.addHeader("Authorization","Bearer " + token);




    }

    /**
     * 로그인 실패시
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("## Login fail");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
