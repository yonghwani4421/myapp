package me.yonghwan.myapp.helper;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.dto.CustomMemberDetails;
import me.yonghwan.myapp.repository.MemberRepository;
import me.yonghwan.myapp.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SessionUtil {
    private final MemberService memberService;

    /**
     * 세션의 email로 조회
     * @return
     * @throws Exception
     */
    public Member getMemberSesson() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomMemberDetails)
            // 사용자를 가져온다.
            return memberService.findByEmail(((CustomMemberDetails) authentication.getPrincipal()).getUsername());
        else
            throw new Exception("세션만료!!");
    }
}
