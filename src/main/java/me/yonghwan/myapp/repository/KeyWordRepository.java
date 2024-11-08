package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KeyWordRepository extends JpaRepository<Keyword, Long> {

    @Query(value = "select k from KeyWord k where k.member.id = :memberId", nativeQuery = true)
    List<Keyword> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query(value = "delete from KeyWord k WHERE k.member.id = :memberId",nativeQuery = true)
    void deleteByMemberId(@Param("memberId") Long memberId);

}
