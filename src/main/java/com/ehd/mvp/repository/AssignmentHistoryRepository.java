package com.ehd.mvp.repository;

import com.ehd.mvp.entity.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentHistoryRepository extends JpaRepository<AssignmentHistory, Long> {
    
    List<AssignmentHistory> findByAssetAssetIdOrderByAssignmentDateDesc(Long assetId);
} 