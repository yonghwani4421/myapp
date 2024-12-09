package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostPhotoRepository  extends JpaRepository<PostPhoto, Long> {


    /**
     * 삭제
     * @param postId
     */
    @Modifying
    void deleteByPostId(Long postId);


    Optional<List<PostPhoto>> findByPostId(Long id);
}
