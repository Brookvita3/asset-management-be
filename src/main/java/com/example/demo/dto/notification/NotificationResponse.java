package com.example.demo.dto.notification;

import com.example.demo.entity.Notification;
import com.example.demo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private Long assetId;
    private String title;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private String linkUrl;
    private Instant createdAt;

    public static NotificationResponse fromEntity(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .assetId(notification.getAsset() != null ? notification.getAsset().getId() : null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .linkUrl(notification.getLinkUrl())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

