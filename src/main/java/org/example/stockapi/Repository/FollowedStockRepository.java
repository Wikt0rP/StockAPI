package org.example.stockapi.Repository;

import org.example.stockapi.Entity.FollowedStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowedStockRepository extends JpaRepository<FollowedStock, Long> {
    List<FollowedStock> findByUserId(Long userId);
    Optional<FollowedStock> findByUserIdAndSymbol(Long userId, String symbol);
    void deleteByUserIdAndSymbol(Long userId, String symbol);
}
