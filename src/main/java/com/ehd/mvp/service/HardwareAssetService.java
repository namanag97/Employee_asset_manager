package com.ehd.mvp.service;

import com.ehd.mvp.dto.CreateAssetRequest;
import com.ehd.mvp.dto.HardwareAssetDto;
import com.ehd.mvp.dto.AssignmentHistoryDto;
import com.ehd.mvp.entity.HardwareAsset;
import com.ehd.mvp.entity.HardwareType;
import com.ehd.mvp.entity.Employee;
import com.ehd.mvp.entity.AssignmentHistory;
import com.ehd.mvp.repository.HardwareAssetRepository;
import com.ehd.mvp.repository.HardwareTypeRepository;
import com.ehd.mvp.repository.EmployeeRepository;
import com.ehd.mvp.repository.AssignmentHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HardwareAssetService {

    private final HardwareAssetRepository hardwareAssetRepository;
    private final HardwareTypeRepository hardwareTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;

    @Transactional(readOnly = true)
    public List<HardwareAssetDto> findAllAssets(String status, Integer typeId, String search) {
        List<HardwareAsset> assets;
        
        // Start with the most specific filter if provided
        if (search != null) {
            assets = hardwareAssetRepository.findByAssetTagContainingIgnoreCaseOrSerialNumberContainingIgnoreCaseOrModelContainingIgnoreCase(
                    search, search, search);
        } else {
            assets = hardwareAssetRepository.findAll();
        }
        
        // Apply additional filters in memory
        if (status != null) {
            assets = assets.stream()
                    .filter(asset -> asset.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        
        if (typeId != null) {
            assets = assets.stream()
                    .filter(asset -> asset.getHardwareType().getTypeId().equals(typeId))
                    .collect(Collectors.toList());
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
        // createdAt and updatedAt will be set automatically by @CreationTimestamp and @UpdateTimestamp

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
        // updatedAt will be set automatically by @UpdateTimestamp

        HardwareAsset updatedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(updatedAsset);
    }

    @Transactional
    public HardwareAssetDto assignAsset(Long assetId, String employeeId) {
        HardwareAsset asset = hardwareAssetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + assetId));

        // Check if asset is available for assignment
        if (!"Available".equals(asset.getStatus())) {
            throw new IllegalStateException("Asset is not available for assignment. Current status: " + asset.getStatus());
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        // Update asset status and assignment
        Instant now = Instant.now();
        asset.setStatus("Assigned");
        asset.setCurrentEmployee(employee);
        asset.setLastAssignmentDate(now);
        // updatedAt will be set automatically by @UpdateTimestamp

        AssignmentHistory history = new AssignmentHistory();
        history.setAsset(asset);
        history.setEmployee(employee);
        history.setAssignmentDate(now);
        // createdAt and updatedAt will be set automatically by @CreationTimestamp and @UpdateTimestamp

        assignmentHistoryRepository.save(history);
        HardwareAsset updatedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(updatedAsset);
    }

    @Transactional
    public HardwareAssetDto returnAsset(Long assetId, String newStatus) {
        HardwareAsset asset = hardwareAssetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Hardware asset not found with id: " + assetId));

        AssignmentHistory latestHistory = assignmentHistoryRepository.findByAssetAssetIdOrderByAssignmentDateDesc(assetId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No assignment history found for asset: " + assetId));

        // Update asset status and clear assignment
        Instant now = Instant.now();
        asset.setStatus(newStatus);
        asset.setCurrentEmployee(null);
        asset.setLastAssignmentDate(null);
        // updatedAt will be set automatically by @UpdateTimestamp

        // Update assignment history
        latestHistory.setReturnDate(now);
        // updatedAt will be set automatically by @UpdateTimestamp

        assignmentHistoryRepository.save(latestHistory);
        HardwareAsset updatedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(updatedAsset);
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
        dto.setTypeId(asset.getHardwareType() != null ? asset.getHardwareType().getTypeId() : null);
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
        dto.setAssignmentDate(history.getAssignmentDate());
        dto.setReturnDate(history.getReturnDate());
        dto.setNotes(history.getNotes());
        dto.setCreatedAt(history.getCreatedAt());
        dto.setUpdatedAt(history.getUpdatedAt());
        
        return dto;
    }
} 