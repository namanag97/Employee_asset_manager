package com.ehd.mvp.service;

import com.ehd.mvp.dto.CreateAssetRequest;
import com.ehd.mvp.dto.HardwareAssetDto;
import com.ehd.mvp.entity.HardwareAsset;
import com.ehd.mvp.entity.HardwareType;
import com.ehd.mvp.repository.HardwareAssetRepository;
import com.ehd.mvp.repository.HardwareTypeRepository;
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
        asset.setCreatedAt(Instant.now());
        asset.setUpdatedAt(Instant.now());

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
        asset.setUpdatedAt(Instant.now());

        HardwareAsset updatedAsset = hardwareAssetRepository.save(asset);
        return convertToDto(updatedAsset);
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
} 