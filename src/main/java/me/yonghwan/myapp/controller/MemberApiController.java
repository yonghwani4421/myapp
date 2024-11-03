package me.yonghwan.myapp.controller;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.MemberRequest;
import me.yonghwan.myapp.dto.MemberResponse;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/join")
    public ResponseEntity<MemberResponse> join(@RequestBody MemberRequest request){
        Member member = memberService.save(request);
        return ResponseEntity.ok().body(new MemberResponse(member));
    }
}
