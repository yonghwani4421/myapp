package me.yonghwan.myapp.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * me.yonghwan.myapp.dto.QPostListResponse is a Querydsl Projection type for PostListResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostListResponse extends ConstructorExpression<PostListResponse> {

    private static final long serialVersionUID = 993914004L;

    public QPostListResponse(com.querydsl.core.types.Expression<Long> postId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> tradeType, com.querydsl.core.types.Expression<Double> price, com.querydsl.core.types.Expression<String> placeName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDate) {
        super(PostListResponse.class, new Class<?>[]{long.class, String.class, String.class, double.class, String.class, java.time.LocalDateTime.class}, postId, title, tradeType, price, placeName, createDate);
    }

}

