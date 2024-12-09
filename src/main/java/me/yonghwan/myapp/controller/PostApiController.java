package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Post;
import me.yonghwan.myapp.domain.Trade;
import me.yonghwan.myapp.dto.*;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.jwt.JWTUtil;
import me.yonghwan.myapp.service.MemberService;
import me.yonghwan.myapp.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "Post API")
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;
    private final SessionUtil sessionUtil;
    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Operation(summary = "거래 게시물 등록", description = "거래 게시물을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<PostResponse> postSave(
            @Parameter(description = "거래 게시물 요청 데이터")
            @RequestPart(value = "post") PostSaveRequest request,

            @Parameter(description = "첨부 파일 리스트", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        Post post = postService.savePostWithAttachment(request.toEntity(sessionUtil.getMemberSesson()), files);
        return CommonResponse.<PostResponse>builder()
                .result(post.toResponse())
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "거래 게시물 수정", description = "거래 게시물을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<PostResponse> postUpdate(
            @Parameter(description = "거래 게시물 id")
            @PathVariable("id") Long postId,

            @Parameter(description = "게시물 요청 데이터")
            @RequestPart(value = "post") PostRequest request,

            @Parameter(description = "추가될 파일")
            @RequestPart(value = "file", required = false)List<MultipartFile> files) {
        Post post = postService.updatePost(request, files, postId);
        return CommonResponse.<PostResponse>builder()
                .result(post.toResponse())
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "거래 게시물 삭제", description = "거래 게시물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @DeleteMapping("/{id}")
    public CommonResponse<Void> postDelete(
            @Parameter(description = "거래 게시물 id")
            @PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "거래 게시물 좋아요", description = "거래 게시물을 좋아요 기능 / 좋아요가 이미 있다면, 취소가된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/{id}/likes")
    public CommonResponse<Void> postLikes(
            @Parameter(description = "거래 게시물 id")
            @PathVariable("id") Long postId,
            @RequestHeader("Authorization") String authorization) {
        Member member = memberService.findByEmail(jwtUtil.getMemberEmail(authorization.replace("Bearer ","")));
        Post post = postService.findByIdWithPhotos(postId);
        postService.addOrCancelLikes(post,member);
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "거래 게시물 거래체결", description = "거래 게시물 거래가 체결됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "체결 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "체결 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/{id}/trade")
    public CommonResponse<PostTradeResponse> trade(
            @Parameter(description = "거래 게시물 id")
            @PathVariable("id") Long postId,
            @RequestHeader("Authorization") String authorization) {
        Member member = memberService.findByEmail(jwtUtil.getMemberEmail(authorization.replace("Bearer ","")));
        Post post = postService.findByIdWithPhotos(postId);
        Trade trade = postService.createTrade(Trade.builder()
                .post(post)
                .buyer(member).build());

        return CommonResponse.<PostTradeResponse>builder()
                .result(PostTradeResponse.createResponse(post,trade.getBuyer().getId()))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }



}
