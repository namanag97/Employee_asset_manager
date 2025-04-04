package com.ehd.mvp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Data
public class AssignmentHistoryDto {
    private Long historyId;
    private Long assetId;
    private String employeeId;
    private String employeeName;
    private Long assignedByUserId;
    private String assignedByUsername;
    private Timestamp assignmentDate;
    private Timestamp returnDate;
    private Long returnedByUserId;
    private String returnedByUsername;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 