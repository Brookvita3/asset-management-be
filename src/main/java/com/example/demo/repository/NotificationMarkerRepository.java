package com.example.demo.repository;

import com.example.demo.entity.NotificationMarker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationMarkerRepository extends JpaRepository<NotificationMarker, Long> {
}

