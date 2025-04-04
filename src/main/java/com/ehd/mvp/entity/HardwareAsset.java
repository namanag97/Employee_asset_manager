package com.ehd.mvp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.sql.Timestamp;

@Entity
@Table(name = "hardware_assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareAsset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;
    
    @Column(name = "asset_tag", nullable = false, unique = true)
    private String assetTag;
    
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;
    
    @Column(name = "make", nullable = false)
    private String make;
    
    @Column(name = "model", nullable = false)
    private String model;
    
    @Column(name = "specifications")
    private String specifications;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "last_assignment_date")
    private Timestamp lastAssignmentDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hardware_type_id", nullable = false)
    private HardwareType hardwareType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_employee_id")
    private Employee currentEmployee;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 