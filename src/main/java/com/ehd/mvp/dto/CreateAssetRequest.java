package com.ehd.mvp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAssetRequest {
    @NotBlank(message = "Asset tag is required")
    @Size(max = 100, message = "Asset tag must be at most 100 characters")
    private String assetTag;

    @NotBlank(message = "Serial number is required")
    @Size(max = 255, message = "Serial number must be at most 255 characters")
    private String serialNumber;

    @NotNull(message = "Hardware type ID is required")
    private Integer typeId;

    @NotBlank(message = "Make is required")
    @Size(max = 100, message = "Make must be at most 100 characters")
    private String make;

    @NotBlank(message = "Model is required")
    @Size(max = 150, message = "Model must be at most 150 characters")
    private String model;

    @Size(max = 1000, message = "Specifications must be at most 1000 characters")
    private String specifications;

    @NotBlank(message = "Status is required")
    private String status;

    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;
} 