package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {


    Member findByEmail(String email);


}
