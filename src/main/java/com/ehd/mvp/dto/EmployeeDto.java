package com.ehd.mvp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmployeeDto {
    private String employeeId;
    private String fullName;
    private String email;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 