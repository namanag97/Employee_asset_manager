package com.ehd.mvp.controller;

import com.ehd.mvp.dto.CreateAssetRequest;
import com.ehd.mvp.dto.HardwareAssetDto;
import com.ehd.mvp.dto.AssignAssetRequest;
import com.ehd.mvp.dto.ReturnAssetRequest;
import com.ehd.mvp.dto.AssignmentHistoryDto;
import com.ehd.mvp.service.HardwareAssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class HardwareAssetController {

    private final HardwareAssetService hardwareAssetService;

    @GetMapping
    public ResponseEntity<List<HardwareAssetDto>> getAllAssets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) String search) {
        List<HardwareAssetDto> assets = hardwareAssetService.findAllAssets(status, typeId, search);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HardwareAssetDto> getAssetById(@PathVariable Long id) {
        HardwareAssetDto asset = hardwareAssetService.findAssetById(id);
        return ResponseEntity.ok(asset);
    }

    @PostMapping
    public ResponseEntity<HardwareAssetDto> createAsset(@Valid @RequestBody CreateAssetRequest request) {
        HardwareAssetDto createdAsset = hardwareAssetService.createAsset(request);
        return new ResponseEntity<>(createdAsset, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HardwareAssetDto> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody CreateAssetRequest request) {
        HardwareAssetDto updatedAsset = hardwareAssetService.updateAsset(id, request);
        return ResponseEntity.ok(updatedAsset);
    }

    @PostMapping("/{assetId}/assign")
    public ResponseEntity<HardwareAssetDto> assignAsset(
            @PathVariable Long assetId,
            @Valid @RequestBody AssignAssetRequest request) {
        HardwareAssetDto assignedAsset = hardwareAssetService.assignAsset(
                assetId, 
                request.getEmployeeId());
        
        return ResponseEntity.ok(assignedAsset);
    }

    @PostMapping("/{assetId}/return")
    public ResponseEntity<HardwareAssetDto> returnAsset(
            @PathVariable Long assetId,
            @Valid @RequestBody ReturnAssetRequest request) {
        HardwareAssetDto returnedAsset = hardwareAssetService.returnAsset(
                assetId,
                request.getReturnStatus());
        
        return ResponseEntity.ok(returnedAsset);
    }

    @GetMapping("/{assetId}/history")
    public ResponseEntity<List<AssignmentHistoryDto>> getAssetHistory(@PathVariable Long assetId) {
        List<AssignmentHistoryDto> history = hardwareAssetService.findAssignmentHistoryByAssetId(assetId);
        return ResponseEntity.ok(history);
    }
} 