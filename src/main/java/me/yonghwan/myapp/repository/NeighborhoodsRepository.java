package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Neighborhoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeighborhoodsRepository extends JpaRepository<Neighborhoods,Long> {
}
