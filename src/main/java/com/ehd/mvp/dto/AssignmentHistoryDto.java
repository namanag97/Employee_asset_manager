package com.ehd.mvp.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class AssignmentHistoryDto {
    private Long historyId;
    private Long assetId;
    private String employeeId;
    private String employeeName;
    private Long assignedByUserId;
    private String assignedByUsername;
    private Instant assignmentDate;
    private Instant returnDate;
    private Long returnedByUserId;
    private String returnedByUsername;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
} 