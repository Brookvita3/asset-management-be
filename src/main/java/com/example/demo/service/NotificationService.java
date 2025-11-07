package com.example.demo.service;

import com.example.demo.dto.notification.NotificationRequest;
import com.example.demo.dto.notification.NotificationResponse;
import com.example.demo.entity.Asset;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public void create(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new DataNotFound("User not found"));

        Asset asset = null;
        if (request.getAssetId() != null) {
            asset = assetRepository.findById(request.getAssetId())
                    .orElseThrow(() -> new DataNotFound("Asset not found"));
        }

        Notification notification = Notification.builder()
                .user(user)
                .asset(asset)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .isRead(Boolean.TRUE.equals(request.getIsRead()))
                .linkUrl(request.getLinkUrl())
                .build();

        notificationRepository.save(notification);
    }

    public void update(Long id, NotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new DataNotFound("User not found"));

        Asset asset = null;
        if (request.getAssetId() != null) {
            asset = assetRepository.findById(request.getAssetId())
                    .orElseThrow(() -> new DataNotFound("Asset not found"));
        }

        notification.setUser(user);
        notification.setAsset(asset);
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setIsRead(Boolean.TRUE.equals(request.getIsRead()));
        notification.setLinkUrl(request.getLinkUrl());

        notificationRepository.save(notification);
    }

    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification not found"));
        notificationRepository.delete(notification);
    }

    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification not found"));
        return NotificationResponse.fromEntity(notification);
    }

    public List<NotificationResponse> getAll() {
        return notificationRepository.findAll().stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }

    public List<NotificationResponse> getByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound("User not found"));
        
        return notificationRepository.findByUser_Id(userId).stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }
}

