package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberResponse;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.repository.RefreshTokenRepository;
import me.yonghwan.myapp.service.RefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Logout", description = "Logout API")
public class LogoutController {

    private final SessionUtil sessionUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/logout")
    public CommonResponse<String> logout() throws Exception {

        Member member = sessionUtil.getMemberSesson();
        /**
         * 레디스에 블랙리스트 등록 검토..
         */



        /**
         * 리프레시 토큰 전체 삭제
         */
        refreshTokenService.deleteAllByMemberId(member.getId());
        /**
         * 세션 제거
         */
        SecurityContextHolder.clearContext();

        return CommonResponse.<String>builder()
                .result("OK")
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

    }
}
