package me.yonghwan.myapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.KeyWord;
import me.yonghwan.myapp.domain.Member;
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
public class MemberApiController {
    private final MemberService memberService;
    private final KeyWordSerive keyWordSerive;
    private final MemberRepository memberRepository;
    private final SessionUtil sessionUtil;



    @PostMapping("/api/members")
    public ApiResponse<MemberResponse> memberJoin(@RequestBody @Valid MemberRequest request) {
        Member member = memberService.save(request);
        return ApiResponse.<MemberResponse>builder()
                .result(new MemberResponse(member))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }
    @PutMapping("/api/members/{id}")
    public ApiResponse<MemberResponse> updateMember(@PathVariable("id") Long id,
                                                    @RequestBody @Valid MemberUpdateRequest request){
        memberService.update(request,id);
        Member findMember = memberService.findById(id);

        return ApiResponse.<MemberResponse>builder()
                .result(new MemberResponse(findMember))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage()).build();
    }

    @GetMapping("/api/members")
    public ApiResponse<List<MemberResponse>> getMembers(){
        List<Member> findMembers = memberService.findMembers();

        return ApiResponse.<List<MemberResponse>>builder().result(findMembers.stream().map(MemberResponse::new).collect(Collectors.toList()))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }

    @GetMapping("/api/members/{id}")
    public ApiResponse<MemberResponse> getMember(@PathVariable("id") Long id){
        Member member = memberService.findById(id);

        return ApiResponse.<MemberResponse>builder().result(new MemberResponse(member))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();
    }

    @PostMapping("/api/members/keywords")
    public ApiResponse<List<KeywordDto>> saveKeywords(@RequestBody List<KeywordDto> request) throws Exception {
        Member member = sessionUtil.getMemberSesson();
        return ApiResponse.<List<KeywordDto>>builder().result(keyWordSerive.saveList(request.stream()
                    .map(o -> KeyWord.builder().content(o.getContent()).member(member).build())
                    .collect(Collectors.toList()))).resultCode(SuccessCode.INSERT_SUCCESS.getStatus()).resultMsg(SuccessCode.INSERT_SUCCESS.getMessage()).build();
    }
//    @GetMapping("/api/members/keywords")
//    public ApiResponse<List<KeywordDto>> saveKeywords() throws Exception {
//        Member member = sessionUtil.getMemberSesson();
//
//
//        return ApiResponse.<List<KeywordDto>>builder().result().resultCode(SuccessCode.INSERT_SUCCESS.getStatus()).resultMsg(SuccessCode.INSERT_SUCCESS.getMessage()).build();
//    }
}
