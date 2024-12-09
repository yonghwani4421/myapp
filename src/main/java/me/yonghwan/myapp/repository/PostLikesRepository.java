package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Member;
import me.yonghwan.myapp.domain.Post;
import me.yonghwan.myapp.domain.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes,Long> {
    Boolean existsByMemberAndPost(Member member, Post post);

    void deleteByMemberAndPost(Member member, Post post);

    Optional<Long> countByPost(Post post);
}
