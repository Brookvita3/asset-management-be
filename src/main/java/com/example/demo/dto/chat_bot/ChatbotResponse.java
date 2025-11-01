package com.example.demo.dto.chat_bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotResponse {

    private String answer;
    private boolean success;
    private String message;

    public static ChatbotResponse success(String answer) {
        return ChatbotResponse.builder()
                .success(true)
                .answer(answer)
                .message("Chat response generated successfully")
                .build();
    }

    public static ChatbotResponse error(String message) {
        return ChatbotResponse.builder()
                .success(false)
                .answer(null)
                .message(message)
                .build();
    }
}