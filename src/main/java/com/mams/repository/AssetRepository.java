package com.mams.repository;

import com.mams.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByBaseId(Long baseId);
    List<Asset> findByCategoryId(Long categoryId);
    List<Asset> findByBaseIdAndCategoryId(Long baseId, Long categoryId);
    Optional<Asset> findByAssetCodeAndBaseId(String assetCode, Long baseId);

    @Query("SELECT COALESCE(SUM(a.quantity), 0) FROM Asset a WHERE (:baseId IS NULL OR a.base.id = :baseId)")
    Long getTotalQuantity(@Param("baseId") Long baseId);
}
