package com.example.demo.dto.chat_bot;

import com.example.demo.enums.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotRequest {

    @NotBlank(message = "Message cannot be blank")
    private String message;

    private Long userId;

    private Role role;
}