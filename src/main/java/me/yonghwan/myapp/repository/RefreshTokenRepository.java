package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByMemberId(Long memberId);

//    RefreshToken findByMemberId(Long memberId);
}
