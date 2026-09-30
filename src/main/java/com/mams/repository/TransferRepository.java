package com.mams.repository;

import com.mams.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByOrderByIdDesc();
    List<Transfer> findByFromBaseIdOrToBaseIdOrderByIdDesc(Long fromBaseId, Long toBaseId);
    List<Transfer> findByFromBaseIdOrToBaseId(Long fromBaseId, Long toBaseId);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.toBase.id = :baseId AND t.status = 'COMPLETED'")
    Long getTotalTransferIn(@Param("baseId") Long baseId);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.fromBase.id = :baseId AND t.status = 'COMPLETED'")
    Long getTotalTransferOut(@Param("baseId") Long baseId);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.status = 'COMPLETED' AND t.toBase.id IS NOT NULL AND (:baseId IS NULL OR t.toBase.id = :baseId)")
    Long getAllTransferIn(@Param("baseId") Long baseId);

    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM Transfer t WHERE t.status = 'COMPLETED' AND t.fromBase.id IS NOT NULL AND (:baseId IS NULL OR t.fromBase.id = :baseId)")
    Long getAllTransferOut(@Param("baseId") Long baseId);
}
