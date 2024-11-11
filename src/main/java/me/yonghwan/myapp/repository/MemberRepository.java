package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {


    Optional<Member> findByEmail(String email);


}
