package com.example.demo.dto.notification;

import com.example.demo.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    private Long assetId;

    @NotBlank(message = "Notification title is required")
    @Size(max = 255, message = "Title must be shorter than 255 characters")
    private String title;

    @NotBlank(message = "Notification message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    private Boolean isRead = Boolean.FALSE;

    private String linkUrl;
}

