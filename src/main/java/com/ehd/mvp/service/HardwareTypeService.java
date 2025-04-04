package com.ehd.mvp.service;

import com.ehd.mvp.dto.HardwareTypeDto;
import com.ehd.mvp.entity.HardwareType;
import com.ehd.mvp.repository.HardwareTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HardwareTypeService {

    private final HardwareTypeRepository hardwareTypeRepository;

    public List<HardwareTypeDto> findAllHardwareTypes() {
        return hardwareTypeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private HardwareTypeDto convertToDto(HardwareType hardwareType) {
        HardwareTypeDto dto = new HardwareTypeDto();
        dto.setTypeId(hardwareType.getTypeId());
        dto.setTypeName(hardwareType.getTypeName());
        return dto;
    }
} 