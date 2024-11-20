package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
