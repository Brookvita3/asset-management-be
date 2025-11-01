package com.example.demo.dto.user;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Long departmentId;
    private String role;
    private Boolean active;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .departmentId(user.getDepartment().getId())
                .role(user.getRole().toString())
                .active(user.getActive())
                .build();
    }
}
