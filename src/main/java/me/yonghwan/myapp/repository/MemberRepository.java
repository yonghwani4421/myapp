package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {




}
