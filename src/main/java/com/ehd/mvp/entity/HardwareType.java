package com.ehd.mvp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hardware_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;
    
    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;
} 