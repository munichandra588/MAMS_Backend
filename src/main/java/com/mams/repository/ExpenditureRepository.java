package com.mams.repository;

import com.mams.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    List<Expenditure> findByBaseId(Long baseId);

    @Query("SELECT COALESCE(SUM(e.quantity), 0) FROM Expenditure e WHERE (:baseId IS NULL OR e.base.id = :baseId)")
    Long getTotalExpended(@Param("baseId") Long baseId);
}
