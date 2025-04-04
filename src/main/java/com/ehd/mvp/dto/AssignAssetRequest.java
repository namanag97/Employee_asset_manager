package com.ehd.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignAssetRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
} 