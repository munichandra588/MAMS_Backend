package com.mams.repository;

import com.mams.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBaseId(Long baseId);

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE (:baseId IS NULL OR p.base.id = :baseId)")
    Long getTotalPurchasedQuantity(@Param("baseId") Long baseId);

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Purchase p WHERE (:baseId IS NULL OR p.base.id = :baseId) AND p.purchaseDate BETWEEN :startDate AND :endDate")
    Long getTotalPurchasedQuantityBetween(@Param("baseId") Long baseId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
