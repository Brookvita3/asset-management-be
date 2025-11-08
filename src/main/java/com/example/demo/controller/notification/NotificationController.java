package com.example.demo.controller.notification;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.notification.NotificationRequest;
import com.example.demo.dto.notification.NotificationResponse;
import com.example.demo.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ResponseObject> createNotification(@Valid @RequestBody NotificationRequest request,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        notificationService.create(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification created successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getNotificationById(@PathVariable Long id) {
        NotificationResponse response = notificationService.getById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllNotifications() {
        List<NotificationResponse> responses = notificationService.getAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(responses)
                .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponse> responses = notificationService.getByUserId(userId);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get notifications by user id successful")
                .data(responses)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateNotification(@PathVariable Long id,
                                                             @Valid @RequestBody NotificationRequest request,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        notificationService.update(id, request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification deleted successfully")
                .build());
    }

    private ResponseEntity<ResponseObject> buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ResponseObject.builder()
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }
}

