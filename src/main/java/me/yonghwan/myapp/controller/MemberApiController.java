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
import me.yonghwan.myapp.common.response.ErrorResponse;
import me.yonghwan.myapp.common.response.CommonResponse;
import me.yonghwan.myapp.domain.Keyword;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Neighborhoods;
import me.yonghwan.myapp.dto.*;
import me.yonghwan.myapp.helper.SessionUtil;
import me.yonghwan.myapp.repository.MemberRepository;
import me.yonghwan.myapp.service.KeyWordSerive;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;
    private final KeyWordSerive keyWordSerive;
    private final MemberRepository memberRepository;
    private final SessionUtil sessionUtil;

    @Operation(summary = "회원가입", description = "회원정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping
    public CommonResponse<MemberResponse> memberJoin(@RequestBody @Valid MemberRequest request) {
        Member member = memberService.save(request);
        return CommonResponse.<MemberResponse>builder()
                .result(new MemberResponse(member))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }
    @Operation(summary = "회원정보 수정", description = "회원정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping("/{id}")
    public CommonResponse<MemberResponse> updateMember(@PathVariable("id") Long id,
                                                       @RequestBody @Valid MemberUpdateRequest request){
        memberService.update(request,id);
        Member findMember = memberService.findById(id);

        return CommonResponse.<MemberResponse>builder()
                .result(new MemberResponse(findMember))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage()).build();
    }
    @Operation(summary = "회원리스트 조회", description = "가입된 회원의 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping
    public CommonResponse<List<MemberResponse>> getMembers(){
        List<Member> findMembers = memberService.findMembers();

        return CommonResponse.<List<MemberResponse>>builder().result(findMembers.stream().map(MemberResponse::new).collect(Collectors.toList()))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }
    @Operation(summary = "회원 조회", description = "회원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/{id}")
    public CommonResponse<MemberDetailDto> getMember(@PathVariable("id") Long id){
        Member member = memberService.findMemberWithKeyWords(id);


        return CommonResponse.<MemberDetailDto>builder().result(MemberDetailDto.from(member))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }
    @Operation(summary = "키워드 저장", description = "키워드를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/keywords")
    public CommonResponse<List<KeywordDto>> saveKeywords(@RequestBody List<KeywordDto> request) throws Exception {
        Member member = sessionUtil.getMemberSesson();
        return CommonResponse.<List<KeywordDto>>builder().result(keyWordSerive.saveList(request.stream()
                    .map(o -> Keyword.builder().content(o.getContent()).member(member).build())
                    .collect(Collectors.toList()))).resultCode(SuccessCode.INSERT_SUCCESS.getStatus()).resultMsg(SuccessCode.INSERT_SUCCESS.getMessage()).build();
    }
    @Operation(summary = "키워드 리스트조회", description = "키워드 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @GetMapping("/keywords")
    public CommonResponse<List<KeywordDto>> saveKeywords() throws Exception {
        Member member = sessionUtil.getMemberSesson();
        return CommonResponse.<List<KeywordDto>>builder()
                .result( keyWordSerive.findByMemberId(member.getId()))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }
    @Operation(summary = "키워드 삭제", description = "키워드를 전체 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @DeleteMapping("/keywords")
    public CommonResponse<Integer> deleteKeywords() throws Exception {
        Member member = sessionUtil.getMemberSesson();

        keyWordSerive.deleteAll(member.getId());
        return CommonResponse.<Integer>builder()
                .result(SuccessCode.DELETE_SUCCESS.ordinal())
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.DELETE_SUCCESS.getMessage()).build();
    }


    @Operation(summary = "우리동네 저장", description = "우리동네를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("/neighborhoods")
    public CommonResponse<NeighborhoodsResponse> saveNeighborhoods(@RequestBody NeighborhoodsRequest request) throws Exception {
        Member member = sessionUtil.getMemberSesson();
        Neighborhoods neighborhoods = memberService.saveNeighborhoods(request, member);

        return CommonResponse.<NeighborhoodsResponse>builder()
                .result(
                        NeighborhoodsResponse.builder()
                                .id(neighborhoods.getId())
                                .placeName(neighborhoods.getPlaceName())
                                .city(neighborhoods.getCity())
                                .latitude(neighborhoods.getLatitude())
                                .longitude(neighborhoods.getLongitude())
                                .zipCode(neighborhoods.getZipCode())
                                .build()
                )
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage()).build();
    }

}
