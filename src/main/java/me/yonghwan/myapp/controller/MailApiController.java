package me.yonghwan.myapp.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.dto.EmailDto;
import me.yonghwan.myapp.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class MailApiController {
    private final MailService mailService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmailDto>> sendEmail(@RequestParam String email) throws MessagingException, UnsupportedEncodingException {
        mailService.sendEmail(email);
        return ResponseEntity.ok(ApiResponse.<EmailDto>builder()
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.getReasonPhrase()).build());
    }
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<Boolean>> confirmEmail(@RequestBody EmailDto emailDto){
        return ResponseEntity.ok(ApiResponse.<Boolean>builder().result(mailService.confirmEmail(emailDto))
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.getReasonPhrase()).build());
    }
}
