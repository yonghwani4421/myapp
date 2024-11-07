package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.KeyWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyWordRepository extends JpaRepository<KeyWord,Long> {
}
