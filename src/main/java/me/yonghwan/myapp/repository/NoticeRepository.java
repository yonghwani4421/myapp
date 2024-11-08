package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
