package com.ehd.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReturnAssetRequest {
    @NotBlank(message = "Return status is required")
    private String returnStatus;
} 