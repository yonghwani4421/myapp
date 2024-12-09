package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 첨부파일과 거래 게시물 함께 조회
     * @param postId
     * @return
     */
    @Query("select p from Post p" +
            " left join fetch" +
            " p.photos" +
            " where p.id = :postId")
    Optional<Post> findByIdWithPhotos(@Param("postId") Long postId);

}
