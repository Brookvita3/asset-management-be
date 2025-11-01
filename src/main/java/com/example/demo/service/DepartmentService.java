package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.department.DepartmentRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.entity.Department;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public void create(DepartmentRequest request) {
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .managerId(request.getManagerId())
                .isActive(request.getIsActive())
                .employeeCount(request.getEmployeeCount())
                .build();
        departmentRepository.save(department);
    }

    public void update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Department not found"));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setManagerId(request.getManagerId());
        department.setIsActive(request.getIsActive());
        department.setEmployeeCount(request.getEmployeeCount());

        departmentRepository.save(department);
    }

    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Department not found"));
        departmentRepository.delete(department);
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Department not found"));
        return DepartmentResponse.fromEntity(department);
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentResponse::fromEntity)
                .toList();
    }
}

