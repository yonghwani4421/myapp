package me.yonghwan.myapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.common.codes.SuccessCode;
import me.yonghwan.myapp.common.response.ApiResponse;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberResponse;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/join")
    public ApiResponse<MemberResponse> join(@RequestBody @Valid MemberRequest request){
        Member member = memberService.save(request);
        return ApiResponse.<MemberResponse>builder().result(new MemberResponse(member))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage()).build();
    }
}
