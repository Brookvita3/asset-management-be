package com.example.demo.dto.department;

import java.time.Instant;

import com.example.demo.entity.Department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private Boolean isActive;
    private Integer employeeCount;
    private Instant createdAt;

    public static DepartmentResponse fromEntity(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .managerId(department.getManagerId())
                .isActive(department.getIsActive())
                .employeeCount(department.getEmployeeCount())
                .createdAt(department.getCreatedAt())
                .build();
    }
}

