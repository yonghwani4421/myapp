package me.yonghwan.myapp.config.exception;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
