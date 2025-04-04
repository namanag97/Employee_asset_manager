package com.ehd.mvp.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class AssignmentHistoryDto {
    private Long historyId;
    private Long assetId;
    private String employeeId;
    private String employeeName;
    private Instant assignmentDate;
    private Instant returnDate;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
} 