package com.ehd.mvp.repository;

import com.ehd.mvp.entity.HardwareType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareTypeRepository extends JpaRepository<HardwareType, Integer> {
} 