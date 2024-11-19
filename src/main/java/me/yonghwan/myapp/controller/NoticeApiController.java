package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notice", description = "Notice API")
public class NoticeApiController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록", description = "공지사항을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/api/admin/notice")
    public CommonResponse<NoticeDto> notice(@RequestBody @Valid NoticeDto request) {
        Notice notice = noticeService.save(request.toEntity());

        return CommonResponse.<NoticeDto>builder()
                .result(new NoticeDto(notice.getId(), notice.getTitle(), notice.getContent()))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }
    @Operation(summary = "공지사항 리스트 조회", description = "공지사항 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/api/notice")
    public CommonResponse<List<NoticeDto>> getNoticeList(){

        return CommonResponse.<List<NoticeDto>>builder()
                .result(noticeService.findAll().stream().map(NoticeDto::new).collect(Collectors.toList()))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "공지사항 디테일 조회", description = "공지사항 세부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/api/notice/{id}")
    public CommonResponse<NoticeDto> getNotice(@PathVariable("id") Long id){
        return CommonResponse.<NoticeDto>builder()
                .result(new NoticeDto(noticeService.findById(id)))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }
    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @DeleteMapping("/api/notice/{id}")
    public CommonResponse<NoticeDto> deleteNotice(@PathVariable("id") Long id) {
        noticeService.delete(id);
        return CommonResponse.<NoticeDto>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }
    @Operation(summary = "공지사항 디테일 수정", description = "공지사항 세부 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping("/api/notice/{id}")
    public CommonResponse<NoticeDto> updateNotice(@PathVariable("id") Long id,
                                                  @RequestBody @Valid NoticeDto request){
        return CommonResponse.<NoticeDto>builder()
                .result(new NoticeDto(noticeService.update(id,request)))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }

}
