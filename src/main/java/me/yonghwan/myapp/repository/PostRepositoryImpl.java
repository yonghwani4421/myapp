package me.yonghwan.myapp.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import me.yonghwan.myapp.domain.QPost;
import me.yonghwan.myapp.dto.PostListResponse;
import me.yonghwan.myapp.dto.PostSearchRequest;
import me.yonghwan.myapp.dto.QPostListResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static me.yonghwan.myapp.domain.QPost.*;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<PostListResponse> searchPostWithSlice(PostSearchRequest searchRequest, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        List<PostListResponse> results = queryFactory
                .select(new QPostListResponse(
                                post.id.as("postId"),
                                post.title,
                                post.tradeType,
                                post.price,
                                post.placeName,
                                post.createDate
                        )
                )
                .from(post)
                .where(
                        titleContains(searchRequest.getTitle()),
                        placeNameEq(searchRequest.getPlaceName()),
                        tradeTypeEq(searchRequest.getTradeType()),
                        priceGoe(searchRequest.getPriceGoe()),
                        priceLoe(searchRequest.getPriceLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        // 다음 페이지가 있는지 확인
        boolean hasNext = results.size() > pageSize;

        if (hasNext){
            results = results.subList(0,pageSize);
        }

        return new SliceImpl<>(results,pageable,hasNext);
    }

    private BooleanExpression priceLoe(Double priceLoe) {
        return priceLoe != null ? post.price.goe(priceLoe) : null;
    }

    private BooleanExpression priceGoe(Double priceGoe) {
        return priceGoe != null ? post.price.goe(priceGoe) : null;
    }

    private BooleanExpression tradeTypeEq(String tradeType) {
        return StringUtils.hasText(tradeType) ? post.tradeType.eq(tradeType) : null;
    }

    private BooleanExpression placeNameEq(String placeName) {
        return StringUtils.hasText(placeName) ? post.placeName.eq(placeName) : null;
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? post.title.contains(title) : null;
    }
}
