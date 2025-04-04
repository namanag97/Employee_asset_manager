package com.ehd.mvp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Data
public class HardwareAssetDto {
    private Long assetId;
    private String assetTag;
    private String serialNumber;
    private Integer typeId;
    private String make;
    private String model;
    private String specifications;
    private String status;
    private String notes;
    private String currentEmployeeId;
    private Timestamp lastAssignmentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 