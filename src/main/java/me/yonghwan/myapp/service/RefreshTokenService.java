package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.RefreshToken;
import me.yonghwan.myapp.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new IllegalArgumentException("not  found : "+ refreshToken));
    }
}
