package me.yonghwan.myapp.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.dto.EmailDto;
import me.yonghwan.myapp.service.MailService;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class MailApiController {
    private final MailService mailService;

    @PostMapping
    public ApiResponse<EmailDto> sendEmail(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        mailService.sendEmail(email);
        return ApiResponse.<EmailDto>builder()
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }
    @PostMapping("/confirm")
    public ApiResponse<Boolean> confirmEmail(@RequestBody EmailDto emailDto){
        return ApiResponse.<Boolean>builder().result(mailService.confirmEmail(emailDto))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }
}
