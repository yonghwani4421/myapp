package me.yonghwan.myapp.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.config.exception.BusinessException;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.RefreshToken;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.dto.TokenDto;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.jwt.JWTUtil;
import me.yonghwan.myapp.repository.RefreshTokenRepository;
import me.yonghwan.myapp.service.MemberService;
import me.yonghwan.myapp.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberService memberService;

    @GetMapping("/api/token")
    public ApiResponse<TokenDto> createToken(@RequestBody Map<String,String> requestBody) throws Exception {
        String refreshToken = requestBody.get("refreshToken");
        Optional<RefreshToken> optionalToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        log.info(refreshToken);
        String email = jwtUtil.getMemberEmail(refreshToken);
        Member member = memberService.findByEmail(email);

        if (optionalToken.isPresent() && !jwtUtil.isExpired(optionalToken.get().getRefreshToken())){
            // access token 발급
            TokenDto response = new TokenDto(jwtUtil.createJwt(member.getEmail(), String.valueOf(member.getRole()), Duration.ofMinutes(3).toMillis()), optionalToken.get().getRefreshToken(), "Baerer");
            return ApiResponse.<TokenDto>builder()
                    .result(response)
                    .resultCode(HttpStatus.OK.value())
                    .resultMsg(HttpStatus.OK.getReasonPhrase())
                    .build();
        } else {
            throw new BusinessException("Refresh token expired.");
        }
    }
}