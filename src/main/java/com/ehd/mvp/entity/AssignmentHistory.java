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
@Table(name = "assignment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;
    
    @Column(name = "assignment_date", nullable = false)
    private Timestamp assignmentDate;
    
    @Column(name = "return_date")
    private Timestamp returnDate;
    
    @Column(name = "notes")
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private HardwareAsset asset;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_user_id")
    private AppUser assignedByUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returned_by_user_id")
    private AppUser returnedByUser;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 