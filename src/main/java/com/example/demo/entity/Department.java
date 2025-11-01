package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "employee_count", nullable = false)
    private Integer employeeCount = 0;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;
}
