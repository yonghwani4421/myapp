package me.yonghwan.myapp.jwt;

import io.jsonwebtoken.Jwts;
import me.yonghwan.myapp.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {


    private SecretKey secretKey;
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * 토큰의 email 검증하는 메소드
     * @param token
     * @return
     */
    public String getMemberEmail(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email",String.class);
    }

    /**
     * 토큰의 role 검증하는 메소드
     * @param token
     * @return
     */
    public String getRole(String token){
        return String.valueOf(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class));
    }
    /**
     * 토큰의 만료일자 검증하는 메소드
     * @param token
     * @return
     */
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }


    /**
     * jwt token 생성
     * @param email
     * @param role
     * @param expiredMs
     * @return
     */
    public String createJwt(String email, String role, Long expiredMs){

        return Jwts.builder()
                .claim("email",email)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
