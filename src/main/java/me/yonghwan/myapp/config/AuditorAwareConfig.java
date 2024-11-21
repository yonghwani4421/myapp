package me.yonghwan.myapp.config;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.helper.SessionUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareConfig implements AuditorAware<String> {
    private final SessionUtil sessionUtil;
    @Override
    public Optional<String> getCurrentAuditor() {
        return sessionUtil.getEmail();
    }
}
