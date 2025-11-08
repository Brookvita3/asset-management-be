package com.example.demo.dto.notification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMarkerRequest {

    @NotNull(message = "Asset id is required")
    private Long assetId;

    @NotNull(message = "Reminder milestone is required")
    private Instant reminderMilestone;
}

