package com.example.demo.dto.notification;

import com.example.demo.entity.NotificationMarker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMarkerResponse {

    private Long id;
    private Long assetId;
    private Instant reminderMilestone;
    private Instant createdAt;

    public static NotificationMarkerResponse fromEntity(NotificationMarker marker) {
        return NotificationMarkerResponse.builder()
                .id(marker.getId())
                .assetId(marker.getAsset() != null ? marker.getAsset().getId() : null)
                .reminderMilestone(marker.getReminderMilestone())
                .createdAt(marker.getCreatedAt())
                .build();
    }
}

