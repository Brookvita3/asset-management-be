package com.example.demo.controller.notification;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.notification.NotificationMarkerRequest;
import com.example.demo.dto.notification.NotificationMarkerResponse;
import com.example.demo.service.NotificationMarkerService;
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
@RequestMapping("/api/v1/notification-markers")
@RequiredArgsConstructor
public class NotificationMarkerController {

    private final NotificationMarkerService notificationMarkerService;

    @PostMapping
    public ResponseEntity<ResponseObject> createNotificationMarker(
            @Valid @RequestBody NotificationMarkerRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        notificationMarkerService.create(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification marker created successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getNotificationMarkerById(@PathVariable Long id) {
        NotificationMarkerResponse response = notificationMarkerService.getById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllNotificationMarkers() {
        List<NotificationMarkerResponse> responses = notificationMarkerService.getAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(responses)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateNotificationMarker(@PathVariable Long id,
            @Valid @RequestBody NotificationMarkerRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        notificationMarkerService.update(id, request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification marker updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteNotificationMarker(@PathVariable Long id) {
        notificationMarkerService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Notification marker deleted successfully")
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

