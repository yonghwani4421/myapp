package me.yonghwan.myapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberResponse;
import me.yonghwan.myapp.dto.MemberUpdateRequest;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

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

        return ApiResponse.<MemberResponse>builder().result(new MemberResponse(findMember))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage()).build();
    }

    @GetMapping("/api/members")
    public ApiResponse<List<MemberResponse>> getMembers(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberResponse> collect = findMembers.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());

        return ApiResponse.<List<MemberResponse>>builder().result(collect)
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

}
