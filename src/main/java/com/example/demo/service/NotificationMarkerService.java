package com.example.demo.service;

import com.example.demo.dto.notification.NotificationMarkerRequest;
import com.example.demo.dto.notification.NotificationMarkerResponse;
import com.example.demo.entity.Asset;
import com.example.demo.entity.NotificationMarker;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.NotificationMarkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationMarkerService {

    private final NotificationMarkerRepository notificationMarkerRepository;
    private final AssetRepository assetRepository;

    public void create(NotificationMarkerRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new DataNotFound("Asset not found"));

        NotificationMarker marker = NotificationMarker.builder()
                .asset(asset)
                .reminderMilestone(request.getReminderMilestone())
                .build();

        notificationMarkerRepository.save(marker);
    }

    public void update(Long id, NotificationMarkerRequest request) {
        NotificationMarker marker = notificationMarkerRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification marker not found"));

        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new DataNotFound("Asset not found"));

        marker.setAsset(asset);
        marker.setReminderMilestone(request.getReminderMilestone());

        notificationMarkerRepository.save(marker);
    }

    public void delete(Long id) {
        NotificationMarker marker = notificationMarkerRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification marker not found"));
        notificationMarkerRepository.delete(marker);
    }

    public NotificationMarkerResponse getById(Long id) {
        NotificationMarker marker = notificationMarkerRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Notification marker not found"));
        return NotificationMarkerResponse.fromEntity(marker);
    }

    public List<NotificationMarkerResponse> getAll() {
        return notificationMarkerRepository.findAll().stream()
                .map(NotificationMarkerResponse::fromEntity)
                .toList();
    }
}

