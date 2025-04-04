package com.ehd.mvp.controller;

import com.ehd.mvp.dto.HardwareTypeDto;
import com.ehd.mvp.service.HardwareTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hardware-types")
@RequiredArgsConstructor
public class HardwareTypeController {

    private final HardwareTypeService hardwareTypeService;

    @GetMapping
    public ResponseEntity<List<HardwareTypeDto>> getAllHardwareTypes() {
        List<HardwareTypeDto> hardwareTypes = hardwareTypeService.findAllHardwareTypes();
        return ResponseEntity.ok(hardwareTypes);
    }
} 