package me.yonghwan.myapp.repository;

import me.yonghwan.myapp.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
