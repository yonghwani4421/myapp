package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.dto.EmailDto;
import me.yonghwan.myapp.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Tag(name = "Mail", description = "Mail API")
public class MailApiController {
    private final MailService mailService;

    @Operation(summary = "회원가입 인증메일", description = "회원가입 인증메일 송신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping
    public CommonResponse<EmailDto> sendEmail(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        mailService.sendEmail(email);
        return CommonResponse.<EmailDto>builder()
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.getReasonPhrase()).build();
    }
    @Operation(summary = "인증메일 확인", description = "메일인증 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/confirm")
    public CommonResponse<Boolean> confirmEmail(@RequestBody EmailDto emailDto){
        return CommonResponse.<Boolean>builder().result(mailService.confirmEmail(emailDto))
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.getReasonPhrase()).build();
    }
}
