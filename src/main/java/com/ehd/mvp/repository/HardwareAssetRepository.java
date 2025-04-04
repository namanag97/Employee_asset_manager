package com.ehd.mvp.repository;

import com.ehd.mvp.entity.HardwareAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareAssetRepository extends JpaRepository<HardwareAsset, Long> {
    
    List<HardwareAsset> findByStatus(String status);
    
    List<HardwareAsset> findByHardwareTypeTypeId(Integer typeId);
    
    List<HardwareAsset> findByAssetTagContainingIgnoreCaseOrSerialNumberContainingIgnoreCaseOrModelContainingIgnoreCase(
            String tag, String serial, String model);
            
    List<HardwareAsset> findByCurrentEmployeeEmployeeId(String employeeId);
} 