package me.yonghwan.myapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.dto.MemberResponse;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.service.NoticeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;
    @PostMapping("/api/notice")
    public ApiResponse<NoticeDto> notice(@RequestBody @Valid NoticeDto request) {
        Notice notice = noticeService.save(request.toEntity());

        return ApiResponse.<NoticeDto>builder()
                .result(new NoticeDto(notice.getId(), notice.getTitle(), notice.getContent()))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }
}
