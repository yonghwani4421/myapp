package me.yonghwan.myapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;
    @PostMapping("/api/admin/notice")
    public ApiResponse<NoticeDto> notice(@RequestBody @Valid NoticeDto request) {
        Notice notice = noticeService.save(request.toEntity());

        return ApiResponse.<NoticeDto>builder()
                .result(new NoticeDto(notice.getId(), notice.getTitle(), notice.getContent()))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }
    @GetMapping("/api/notice")
    public ApiResponse<List<NoticeDto>> getNoticeList(){

        return ApiResponse.<List<NoticeDto>>builder()
                .result(noticeService.findAll().stream().map(NoticeDto::new).collect(Collectors.toList()))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/api/notice/{id}")
    public ApiResponse<NoticeDto> getNotice(@PathVariable("id") Long id){
        return ApiResponse.<NoticeDto>builder()
                .result(new NoticeDto(noticeService.findById(id)))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }
    @DeleteMapping("/api/notice/{id}")
    public ApiResponse<NoticeDto> deleteNotice(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return ApiResponse.<NoticeDto>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }
    @PutMapping("/api/notice/{id}")
    public ApiResponse<NoticeDto> updateNotice(@PathVariable("id") Long id,
                                               @RequestBody @Valid NoticeDto request){
        return ApiResponse.<NoticeDto>builder()
                .result(new NoticeDto(noticeService.update(id,request)))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }

}
