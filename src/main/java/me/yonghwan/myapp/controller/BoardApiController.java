package me.yonghwan.myapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.dto.*;
import me.yonghwan.myapp.service.BoardService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "Board API")
@RequestMapping("/api/board")
public class BoardApiController {

    private final BoardService boardService;
    @Operation(summary = "게시물 등록", description = "게시물을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping
    public CommonResponse<BoardResponse> boardSave(
            @RequestPart(value = "board") BoardRequest request
            , @RequestPart(value = "file", required = false)List<MultipartFile> files) throws FileUploadException {
        Board board = boardService.saveBoardWithAttachments(request.toEntity(), files);
        return CommonResponse.<BoardResponse>builder()
                .result(
                        new BoardResponse(board.getTitle(),board.getContent()
                                ,board.getBoardAttachments().stream().map(BoardAttachmentResponse::new).collect(Collectors.toList()))
                )
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping("/{id}")
    public CommonResponse<BoardResponse> boardUpdate(
            @PathVariable("id") Long id
            , @RequestPart(value = "board") BoardRequest request
            , @RequestPart(value = "file", required = false)List<MultipartFile> files) {
        Board board = boardService.updateBoard(request, files, id);
        return CommonResponse.<BoardResponse>builder()
                .result(
                        new BoardResponse(board.getTitle(),board.getContent()
                                ,board.getBoardAttachments().stream().map(BoardAttachmentResponse::new).collect(Collectors.toList()))
                )
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @DeleteMapping("/{id}")
    public CommonResponse<Void> boardDelete(@PathVariable("id") Long id) {
        boardService.deleteBoardById(id);
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }





}
