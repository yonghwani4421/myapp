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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<ApiResponse<MemberResponse>> memberJoin(@RequestBody @Valid MemberRequest request) {
        Member member = memberService.save(request);
        ApiResponse<MemberResponse> response = ApiResponse.<MemberResponse>builder()
                .result(new MemberResponse(member))
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.status(SuccessCode.INSERT_SUCCESS.getStatus()).body(response);
    }
    @PutMapping("/api/members/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@PathVariable("id") Long id,
                                                    @RequestBody @Valid MemberUpdateRequest request){
        memberService.update(request,id);
        Member findMember = memberService.findById(id);

        ApiResponse<MemberResponse> response = ApiResponse.<MemberResponse>builder().result(new MemberResponse(findMember))
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMsg(SuccessCode.UPDATE_SUCCESS.getMessage()).build();


        return ResponseEntity.status(SuccessCode.UPDATE_SUCCESS.getStatus()).body(response);
    }

    @GetMapping("/api/members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getMembers(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberResponse> collect = findMembers.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());

        ApiResponse<List<MemberResponse>> response = ApiResponse.<List<MemberResponse>>builder().result(collect)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();

        return ResponseEntity.status(SuccessCode.SELECT_SUCCESS.getStatus()).body(response);
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable("id") Long id){
        Member member = memberService.findById(id);

        ApiResponse<MemberResponse> response = ApiResponse.<MemberResponse>builder().result(new MemberResponse(member))
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage()).build();

        return ResponseEntity.status(SuccessCode.SELECT_SUCCESS.getStatus()).body(response);
    }

}
