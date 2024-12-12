package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Post;
import me.yonghwan.myapp.dto.PostDetailResponse;
import me.yonghwan.myapp.dto.PostListResponse;
import me.yonghwan.myapp.dto.PostResponse;
import me.yonghwan.myapp.dto.PostSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {


    /**
     * 거래게시물 리스트 조회
     * 동적 쿼리
     * @param searchRequest
     * @param pageable
     * @return
     */
    Slice<PostListResponse> searchPostWithSlice(PostSearchRequest searchRequest, Pageable pageable);


}
