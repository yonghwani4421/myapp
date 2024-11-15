package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Manner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MannerRepository extends JpaRepository<Manner, Long> {
}
