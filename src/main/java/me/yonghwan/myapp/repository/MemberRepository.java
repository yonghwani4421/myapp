package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {


    Optional<Member> findByEmail(String email);

    @Query("select m from Member m" +
            " left join fetch m.keyWords" +
            " where m.id = :id")
    Optional<Member> findMemberWithKeyWords(@Param("id") Long id);


}
