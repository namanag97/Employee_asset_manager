package com.ehd.mvp.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class EmployeeDto {
    private String employeeId;
    private String fullName;
    private String email;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
} 