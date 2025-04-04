package com.ehd.mvp.controller;

import com.ehd.mvp.dto.CreateEmployeeRequest;
import com.ehd.mvp.dto.EmployeeDto;
import com.ehd.mvp.dto.HardwareAssetDto;
import com.ehd.mvp.service.EmployeeService;
import com.ehd.mvp.service.HardwareAssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final HardwareAssetService hardwareAssetService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String employeeId) {
        EmployeeDto employee = employeeService.findEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeDto createdEmployee = employeeService.createEmployee(request);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable String employeeId,
            @Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @GetMapping("/{employeeId}/assets")
    public ResponseEntity<List<HardwareAssetDto>> getEmployeeAssets(@PathVariable String employeeId) {
        List<HardwareAssetDto> assets = hardwareAssetService.findAssetsByEmployeeId(employeeId);
        return ResponseEntity.ok(assets);
    }
} 