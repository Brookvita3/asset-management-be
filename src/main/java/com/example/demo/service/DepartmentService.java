package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.department.DepartmentRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.entity.Department;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.enums.NotificationType;
import com.example.demo.enums.Role;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

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

        // Tạo notification cho manager của department (nếu có)
        if (department.getManagerId() != null) {
            User manager = userRepository.findById(department.getManagerId())
                    .orElse(null);
            if (manager != null) {
                Notification notification = Notification.builder()
                        .user(manager)
                        .asset(null)
                        .title("Phòng ban đã được cập nhật")
                        .message(String.format(
                                "Phòng ban %s đã được cập nhật thông tin. Tên phòng ban: %s, Mô tả: %s, Số nhân viên: %d",
                                department.getName(), department.getName(),
                                department.getDescription() != null ? department.getDescription() : "Không có mô tả",
                                department.getEmployeeCount()))
                        .type(NotificationType.INFO)
                        .isRead(false)
                        .build();
                notificationRepository.save(notification);
            }
        }
    }

    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Department not found"));
        departmentRepository.delete(department);
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Department not found"));

        Integer employeeCount = Math.toIntExact(userRepository.countByDepartmentId(department.getId()));
        department.setEmployeeCount(employeeCount);
        return DepartmentResponse.fromEntity(department);
    }

    public List<DepartmentResponse> getAll() {
        // Update employee counts before returning
        List<Department> departments = departmentRepository.findAll();

        for (Department dept : departments) {
            Integer employeeCount = Math.toIntExact(userRepository.countByDepartmentId(dept.getId()));
            dept.setEmployeeCount(employeeCount);
            // nếu manager id null thì tìm manager bằng cách tìm user có department id =
            // dept id và role = "MANAGER"
            if (dept.getManagerId() == null) {
                var manager = userRepository.findByDepartmentIdAndRole(dept.getId(), Role.MANAGER);
                if (manager != null) {
                    dept.setManagerId(manager.getId());
                }
            }

        }
        return departments.stream()
                .map(DepartmentResponse::fromEntity)
                .toList();
    }
}
