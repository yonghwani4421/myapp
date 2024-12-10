package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.dto.PostListResponse;
import me.yonghwan.myapp.dto.PostResponse;
import me.yonghwan.myapp.dto.PostSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {


    Slice<PostListResponse> searchPostWithSlice(PostSearchRequest searchRequest, Pageable pageable);
}
