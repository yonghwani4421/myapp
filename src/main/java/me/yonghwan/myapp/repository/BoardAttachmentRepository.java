package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.BoardAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardAttachmentRepository extends JpaRepository<BoardAttachment, Long> {
}
