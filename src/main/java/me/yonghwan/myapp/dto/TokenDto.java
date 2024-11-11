package me.yonghwan.myapp.dto;

import lombok.*;

@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public TokenDto(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }
}
