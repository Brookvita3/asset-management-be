package com.example.demo.entity;

import com.example.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String email;

    private String department;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer status;
}
