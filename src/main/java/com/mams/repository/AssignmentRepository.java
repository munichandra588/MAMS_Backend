package com.mams.repository;

import com.mams.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByBaseId(Long baseId);

    @Query("SELECT COALESCE(SUM(a.quantity), 0) FROM Assignment a WHERE a.status = 'ACTIVE' AND (:baseId IS NULL OR a.base.id = :baseId)")
    Long getTotalAssigned(@Param("baseId") Long baseId);
}
