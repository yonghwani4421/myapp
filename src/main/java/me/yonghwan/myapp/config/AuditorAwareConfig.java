package me.yonghwan.myapp.config;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.helper.SessionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AuditorAwareConfig {
    private final SessionUtil sessionUtil;
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(sessionUtil.getEmail());
    }
}
