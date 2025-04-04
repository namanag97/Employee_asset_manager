package com.ehd.mvp.service;

import com.ehd.mvp.entity.*;
import com.ehd.mvp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HardwareAssetServiceTest {

    @Mock
    private HardwareAssetRepository hardwareAssetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AssignmentHistoryRepository assignmentHistoryRepository;

    @InjectMocks
    private HardwareAssetService hardwareAssetService;

    private HardwareAsset availableAsset;
    private Employee employee;
    private AppUser assigningUser;

    @BeforeEach
    void setUp() {
        // Create test data
        availableAsset = new HardwareAsset();
        availableAsset.setAssetId(1L);
        availableAsset.setStatus("Available");
        availableAsset.setUpdatedAt(LocalDateTime.now());

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFullName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setStatus("Active");

        assigningUser = new AppUser();
        assigningUser.setUserId(1L);
        assigningUser.setUsername("admin");
        assigningUser.setEmail("admin@example.com");
        assigningUser.setRole("ADMIN");
    }

    @Test
    void assignAsset_Success() {
        // Arrange
        when(hardwareAssetRepository.findById(1L)).thenReturn(java.util.Optional.of(availableAsset));
        when(employeeRepository.findById("EMP001")).thenReturn(java.util.Optional.of(employee));
        when(appUserRepository.findById(1L)).thenReturn(java.util.Optional.of(assigningUser));
        when(hardwareAssetRepository.save(any(HardwareAsset.class))).thenReturn(availableAsset);
        when(assignmentHistoryRepository.save(any(AssignmentHistory.class))).thenReturn(new AssignmentHistory());

        // Act
        var result = hardwareAssetService.assignAsset(1L, "EMP001", 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Assigned", availableAsset.getStatus());
        assertEquals(employee, availableAsset.getCurrentEmployee());
        assertNotNull(availableAsset.getLastAssignmentDate());

        // Verify repository interactions
        verify(hardwareAssetRepository).findById(1L);
        verify(employeeRepository).findById("EMP001");
        verify(appUserRepository).findById(1L);
        verify(hardwareAssetRepository).save(availableAsset);
        verify(assignmentHistoryRepository).save(any(AssignmentHistory.class));
    }

    @Test
    void assignAsset_AssetNotFound() {
        // Arrange
        when(hardwareAssetRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            hardwareAssetService.assignAsset(1L, "EMP001", 1L);
        });

        verify(hardwareAssetRepository).findById(1L);
        verifyNoMoreInteractions(hardwareAssetRepository, employeeRepository, appUserRepository, assignmentHistoryRepository);
    }

    @Test
    void assignAsset_EmployeeNotFound() {
        // Arrange
        when(hardwareAssetRepository.findById(1L)).thenReturn(java.util.Optional.of(availableAsset));
        when(employeeRepository.findById("EMP001")).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            hardwareAssetService.assignAsset(1L, "EMP001", 1L);
        });

        verify(hardwareAssetRepository).findById(1L);
        verify(employeeRepository).findById("EMP001");
        verifyNoMoreInteractions(hardwareAssetRepository, employeeRepository, appUserRepository, assignmentHistoryRepository);
    }

    @Test
    void assignAsset_UserNotFound() {
        // Arrange
        when(hardwareAssetRepository.findById(1L)).thenReturn(java.util.Optional.of(availableAsset));
        when(employeeRepository.findById("EMP001")).thenReturn(java.util.Optional.of(employee));
        when(appUserRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            hardwareAssetService.assignAsset(1L, "EMP001", 1L);
        });

        verify(hardwareAssetRepository).findById(1L);
        verify(employeeRepository).findById("EMP001");
        verify(appUserRepository).findById(1L);
        verifyNoMoreInteractions(hardwareAssetRepository, employeeRepository, appUserRepository, assignmentHistoryRepository);
    }

    @Test
    void assignAsset_AssetNotAvailable() {
        // Arrange
        availableAsset.setStatus("Assigned");
        when(hardwareAssetRepository.findById(1L)).thenReturn(java.util.Optional.of(availableAsset));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            hardwareAssetService.assignAsset(1L, "EMP001", 1L);
        });

        verify(hardwareAssetRepository).findById(1L);
        verifyNoMoreInteractions(hardwareAssetRepository, employeeRepository, appUserRepository, assignmentHistoryRepository);
    }
} 