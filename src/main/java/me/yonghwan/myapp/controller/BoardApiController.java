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
import me.yonghwan.myapp.domain.BoardComment;
import me.yonghwan.myapp.dto.*;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.service.BoardCommentService;
import me.yonghwan.myapp.service.BoardService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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
    private final SessionUtil sessionUtil;
    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시물 리스트", description = "게시물 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping
    public CommonResponse<Page<BoardListResponse>> getList(
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(name = "pageNumber") int pageNumber,

            @Parameter(description = "페이지 크기")
            @RequestParam(name = "pageSize") int pageSize){

        Page<Board> boardList = boardService.getBoardList(PageRequest.of(pageNumber, pageSize));
        return CommonResponse.<Page<BoardListResponse>>builder()
                .result(
                        boardList.map(board ->
                                new BoardListResponse(
                                        board.getId(),
                                        board.getTitle(),
                                        board.getContent(),
                                        board.getCreateDate()))
                )
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }


//    @Operation(summary = "게시물 디테일", description = "게시물 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
//            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
//    })
//    @GetMapping
//    public CommonResponse<BoardResponse> getBoard(
//                        @Parameter(description = "게시물 id")
//                        @PathVariable("id") Long boardId){
//
//
//
//
//        return CommonResponse.<BoardResponse>builder()
//                .result(
//
//                )
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//                .build();
//    }

    @Operation(summary = "게시물 등록", description = "게시물을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<BoardResponse> boardSave(
            @Parameter(description = "게시물 요청 데이터")
            @RequestPart(value = "board") BoardSaveRequest request,

            @Parameter(description = "첨부 파일 리스트", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {



        Board board = boardService.saveBoardWithAttachments(request.toEntity(sessionUtil.getMemberSesson()), files);

        return CommonResponse.<BoardResponse>builder()
                .result(
                        new BoardResponse(board.getId(), board.getTitle(), board.getContent(),
                                board.getBoardAttachments().stream().map(BoardAttachmentResponse::new).collect(Collectors.toList()))
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
            @Parameter(description = "게시물 id")
            @PathVariable("id") Long boardId,

            @Parameter(description = "게시물 요청 데이터")
            @RequestPart(value = "board") BoardRequest request,

            @Parameter(description = "추가될 파일")
            @RequestPart(value = "file", required = false)List<MultipartFile> files) {
        Board board = boardService.updateBoard(request, files, boardId);
        return CommonResponse.<BoardResponse>builder()
                .result(
                        new BoardResponse(board.getId(), board.getTitle(),board.getContent()
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
    public CommonResponse<Void> boardDelete(
            @Parameter(description = "게시물 id")
            @PathVariable("id") Long boardId) {
        boardService.deleteBoardById(boardId);
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "게시물 상태 변경 삭제", description = "게시물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping("/{id}/status")
    public CommonResponse<Void> boardStatusChange(
            @Parameter(description = "게시물 id")
            @PathVariable("id") Long boardId) {
        boardService.changeStatus(boardId);
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }


    @Operation(summary = "게시물 좋아요", description = "게시물 좋아요 / 좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/{id}/likes")
    public CommonResponse<Void> boardLike(
            @Parameter(description = "게시물 id")
            @PathVariable("id") Long boardId) {
        boardService.addOrCancelLikes(boardId, sessionUtil.getMemberSesson());
        return CommonResponse.<Void>builder()
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }


    @Operation(summary = "게시물 댓글 작성", description = "게시물 댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/{id}/comment")
    public CommonResponse<BoardCommentResponse> boardComment(
            @Parameter(description = "게시물 id")
            @PathVariable("id") Long boardId,

            @Parameter(description = "게시물 댓글 요청값")
            @RequestBody BoardCommentRequest request) {

        return CommonResponse.<BoardCommentResponse>builder()
                .result(
                        new BoardCommentResponse(boardCommentService.createComment(request, boardId))
                )
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }



}
