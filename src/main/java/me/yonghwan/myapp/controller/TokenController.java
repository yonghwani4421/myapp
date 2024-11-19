package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.config.exception.BusinessException;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.RefreshToken;
import me.yonghwan.myapp.dto.TokenDto;
import me.yonghwan.myapp.jwt.JWTUtil;
import me.yonghwan.myapp.repository.RefreshTokenRepository;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Token", description = "Token API")
@Slf4j
public class TokenController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberService memberService;

    @Operation(summary = "토큰 발급", description = "jwt 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/api/token")
    public CommonResponse<TokenDto> createToken(@RequestBody Map<String,String> requestBody) throws Exception {
        String refreshToken = requestBody.get("refreshToken");
        Optional<RefreshToken> optionalToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        log.info(refreshToken);
        String email = jwtUtil.getMemberEmail(refreshToken);
        Member member = memberService.findByEmail(email);

        if (optionalToken.isPresent() && !jwtUtil.isExpired(optionalToken.get().getRefreshToken())){
            // access token 발급
            TokenDto response = new TokenDto(jwtUtil.createJwt(member.getEmail(), String.valueOf(member.getRole()), Duration.ofMinutes(3).toMillis()), optionalToken.get().getRefreshToken(), "Baerer");
            return CommonResponse.<TokenDto>builder()
                    .result(response)
                    .resultCode(HttpStatus.OK.value())
                    .resultMsg(HttpStatus.OK.getReasonPhrase())
                    .build();
        } else {
            throw new BusinessException("Refresh token expired.");
        }
    }
}
