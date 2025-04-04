package com.ehd.mvp.service;

import com.ehd.mvp.dto.CreateAssetRequest;
import com.ehd.mvp.dto.HardwareAssetDto;
import com.ehd.mvp.dto.AssignmentHistoryDto;
import com.ehd.mvp.entity.HardwareAsset;
import com.ehd.mvp.entity.HardwareType;
import com.ehd.mvp.entity.Employee;
import com.ehd.mvp.entity.AppUser;
import com.ehd.mvp.entity.AssignmentHistory;
import com.ehd.mvp.repository.HardwareAssetRepository;
import com.ehd.mvp.repository.HardwareTypeRepository;
import com.ehd.mvp.repository.EmployeeRepository;
import com.ehd.mvp.repository.AppUserRepository;
import com.ehd.mvp.repository.AssignmentHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HardwareAssetService {

    private final HardwareAssetRepository hardwareAssetRepository;
    private final HardwareTypeRepository hardwareTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final AppUserRepository appUserRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;

    @Transactional(readOnly = true)
    public List<HardwareAssetDto> findAllAssets(String status, Integer typeId, String search) {
        List<HardwareAsset> assets;
        
        if (status != null && typeId != null) {
            assets = hardwareAssetRepository.findByStatusAndHardwareTypeTypeId(status, typeId);
        } else if (status != null) {
            assets = hardwareAssetRepository.findByStatus(status);
        } else if (typeId != null) {
            assets = hardwareAssetRepository.findByHardwareTypeTypeId(typeId);
        } else if (search != null) {
            assets = hardwareAssetRepository.findByAssetTagContainingIgnoreCaseOrSerialNumberContainingIgnoreCaseOrModelContainingIgnoreCase(
                    search, search, search);
        } else {
            assets = hardwareAssetRepository.findAll();
        }
        
        return assets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HardwareAssetDto findAssetById(Long id) {
        return hardwareAssetRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + id));
    }

    @Transactional
    public HardwareAssetDto createAsset(CreateAssetRequest request) {
        HardwareType hardwareType = hardwareTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Hardware type not found with id: " + request.getTypeId()));

        HardwareAsset asset = new HardwareAsset();
        asset.setAssetTag(request.getAssetTag());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setHardwareType(hardwareType);
        asset.setMake(request.getMake());
        asset.setModel(request.getModel());
        asset.setSpecifications(request.getSpecifications());
        asset.setStatus(request.getStatus());
        asset.setNotes(request.getNotes());
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());

        HardwareAsset savedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(savedAsset);
    }

    @Transactional
    public HardwareAssetDto updateAsset(Long id, CreateAssetRequest request) {
        HardwareAsset asset = hardwareAssetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + id));

        HardwareType hardwareType = hardwareTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Hardware type not found with id: " + request.getTypeId()));

        asset.setAssetTag(request.getAssetTag());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setHardwareType(hardwareType);
        asset.setMake(request.getMake());
        asset.setModel(request.getModel());
        asset.setSpecifications(request.getSpecifications());
        asset.setStatus(request.getStatus());
        asset.setNotes(request.getNotes());
        asset.setUpdatedAt(LocalDateTime.now());

        HardwareAsset updatedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(updatedAsset);
    }

    @Transactional
    public HardwareAssetDto assignAsset(Long assetId, String employeeId, Long assigningUserId) {
        // Find the asset and verify it's available
        HardwareAsset asset = hardwareAssetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + assetId));
        
        if (!"Available".equals(asset.getStatus())) {
            throw new IllegalStateException("Asset must be in 'Available' status to be assigned");
        }

        // Find the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        // Find the assigning user
        AppUser assigningUser = appUserRepository.findById(assigningUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigningUserId));

        // Update asset status and assignment
        LocalDateTime now = LocalDateTime.now();
        asset.setStatus("Assigned");
        asset.setCurrentEmployee(employee);
        asset.setLastAssignmentDate(Timestamp.valueOf(now));
        asset.setUpdatedAt(now);

        // Create assignment history record
        AssignmentHistory history = new AssignmentHistory();
        history.setAsset(asset);
        history.setEmployee(employee);
        history.setAssignedByUser(assigningUser);
        history.setAssignmentDate(Timestamp.valueOf(now));

        // Save changes
        hardwareAssetRepository.save(asset);
        assignmentHistoryRepository.save(history);

        return convertToDto(asset);
    }

    @Transactional
    public HardwareAssetDto returnAsset(Long assetId, String newStatus, Long returningUserId) {
        // Find the asset and verify it's assigned
        HardwareAsset asset = hardwareAssetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + assetId));
        
        if (!"Assigned".equals(asset.getStatus())) {
            throw new IllegalStateException("Asset must be in 'Assigned' status to be returned");
        }

        // Find the returning user
        AppUser returningUser = appUserRepository.findById(returningUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + returningUserId));

        // Find the latest assignment history record
        AssignmentHistory latestHistory = assignmentHistoryRepository.findByAssetAssetIdOrderByAssignmentDateDesc(assetId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No assignment history found for asset: " + assetId));

        // Update asset status and clear assignment
        LocalDateTime now = LocalDateTime.now();
        asset.setStatus(newStatus);
        asset.setCurrentEmployee(null);
        asset.setLastAssignmentDate(null);
        asset.setUpdatedAt(now);

        // Update assignment history
        latestHistory.setReturnDate(Timestamp.valueOf(now));
        latestHistory.setReturnedByUser(returningUser);

        // Save changes
        hardwareAssetRepository.save(asset);
        assignmentHistoryRepository.save(latestHistory);

        return convertToDto(asset);
    }

    @Transactional(readOnly = true)
    public List<HardwareAssetDto> findAssetsByEmployeeId(String employeeId) {
        List<HardwareAsset> assets = hardwareAssetRepository.findByCurrentEmployeeEmployeeId(employeeId);
        return assets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentHistoryDto> findAssignmentHistoryByAssetId(Long assetId) {
        List<AssignmentHistory> history = assignmentHistoryRepository.findByAssetAssetIdOrderByAssignmentDateDesc(assetId);
        return history.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private HardwareAssetDto convertToDto(HardwareAsset asset) {
        HardwareAssetDto dto = new HardwareAssetDto();
        dto.setAssetId(asset.getAssetId());
        dto.setAssetTag(asset.getAssetTag());
        dto.setSerialNumber(asset.getSerialNumber());
        dto.setTypeId(asset.getHardwareType().getTypeId());
        dto.setMake(asset.getMake());
        dto.setModel(asset.getModel());
        dto.setSpecifications(asset.getSpecifications());
        dto.setStatus(asset.getStatus());
        dto.setNotes(asset.getNotes());
        dto.setCurrentEmployeeId(asset.getCurrentEmployee() != null ? asset.getCurrentEmployee().getEmployeeId() : null);
        dto.setLastAssignmentDate(asset.getLastAssignmentDate());
        dto.setCreatedAt(asset.getCreatedAt());
        dto.setUpdatedAt(asset.getUpdatedAt());
        return dto;
    }

    private AssignmentHistoryDto convertToDto(AssignmentHistory history) {
        AssignmentHistoryDto dto = new AssignmentHistoryDto();
        dto.setHistoryId(history.getHistoryId());
        dto.setAssetId(history.getAsset().getAssetId());
        dto.setEmployeeId(history.getEmployee().getEmployeeId());
        dto.setEmployeeName(history.getEmployee().getFullName());
        
        if (history.getAssignedByUser() != null) {
            dto.setAssignedByUserId(history.getAssignedByUser().getUserId());
            dto.setAssignedByUsername(history.getAssignedByUser().getUsername());
        }
        
        dto.setAssignmentDate(history.getAssignmentDate());
        dto.setReturnDate(history.getReturnDate());
        
        if (history.getReturnedByUser() != null) {
            dto.setReturnedByUserId(history.getReturnedByUser().getUserId());
            dto.setReturnedByUsername(history.getReturnedByUser().getUsername());
        }
        
        dto.setNotes(history.getNotes());
        dto.setCreatedAt(history.getCreatedAt());
        dto.setUpdatedAt(history.getUpdatedAt());
        
        return dto;
    }
} 