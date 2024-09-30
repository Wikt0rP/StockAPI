package org.example.stockapi.Repository;

import org.example.stockapi.Entity.FollowedCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowedCurrencyRepository extends JpaRepository<FollowedCurrency, Long> {
    List<FollowedCurrency> findByUserId(Long userId);
    Optional<FollowedCurrency> findByUserIdAndSymbol(Long userId, String symbol);
    void deleteByUserIdAndSymbol(Long userId, String symbol);
}
