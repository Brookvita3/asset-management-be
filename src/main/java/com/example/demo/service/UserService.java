package com.example.demo.service;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.entity.Department;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.enums.NotificationType;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationRepository notificationRepository;

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
        User savedUser = userRepository.save(user);

        // Gửi thông báo cho người dùng mới được tạo
        createNotificationForUser(savedUser, NotificationType.USER_CREATED);
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
        User updatedUser = userRepository.save(user);

        // Gửi thông báo cho người dùng được cập nhật
        createNotificationForUser(updatedUser, NotificationType.USER_UPDATED);
    }

    public List<UserResponse> getByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return userRepository.findByDepartmentId(department.getId()).stream()
                .map(UserResponse::fromUser)
                .toList();
    }

    /**
     * Tạo thông báo cho người dùng khi admin thêm hoặc cập nhật thông tin
     */
    private void createNotificationForUser(User user, NotificationType type) {
        String title;
        String message;

        if (type == NotificationType.USER_CREATED) {
            title = "Tài khoản của bạn đã được tạo";
            message = String.format("Xin chào %s, tài khoản của bạn đã được tạo thành công bởi quản trị viên. " +
                    "Email: %s, Phòng ban: %s, Vai trò: %s",
                    user.getName(),
                    user.getEmail(),
                    user.getDepartment() != null ? user.getDepartment().getName() : "N/A",
                    user.getRole());
        } else if (type == NotificationType.USER_UPDATED) {
            title = "Thông tin tài khoản của bạn đã được cập nhật";
            message = String.format("Xin chào %s, thông tin tài khoản của bạn đã được cập nhật bởi quản trị viên. " +
                    "Phòng ban: %s, Vai trò: %s",
                    user.getName(),
                    user.getDepartment() != null ? user.getDepartment().getName() : "N/A",
                    user.getRole());
        } else {
            return; // Không tạo thông báo cho các loại khác
        }

        Notification notification = Notification.builder()
                .user(user)
                .asset(null)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .linkUrl("/profile") // Link đến trang profile của user
                .build();

        notificationRepository.save(notification);
    }
}
