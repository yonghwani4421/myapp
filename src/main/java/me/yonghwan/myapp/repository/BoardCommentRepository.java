package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.BoardAttachment;
import me.yonghwan.myapp.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
}
