package com.example.demo.service;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.entity.Department;
import com.example.demo.entity.User;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(UserResponse::fromUser).toList();
    }

    public UserResponse getById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::fromUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void create(UserRequest userRequest) {

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = User.builder()
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .active(true)
                .department(department)
                .build();
        userRepository.save(user);
    }

    public void update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        user.setName(userRequest.getName());
        user.setRole(userRequest.getRole());
        user.setDepartment(department);
        user.setActive(userRequest.getActive());
        userRepository.save(user);
    }

    public List<UserResponse> getByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return userRepository.findByDepartmentId(department.getId()).stream()
                .map(UserResponse::fromUser)
                .toList();
    }
}
