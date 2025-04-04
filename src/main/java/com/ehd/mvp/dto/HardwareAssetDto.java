package com.ehd.mvp.dto;

import lombok.Data;
import java.time.Instant;

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
    private Instant lastAssignmentDate;
    private Instant createdAt;
    private Instant updatedAt;
} 