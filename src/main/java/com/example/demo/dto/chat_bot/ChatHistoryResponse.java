package com.example.demo.dto.chat_bot;

import com.example.demo.entity.chat_bot.MessageDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatHistoryResponse {

    private Long id;
    private String content;
    private MessageDirection direction;
    private Instant timestamp;
    private Integer tokensUsed;

    // Factory method để chuyển từ Message entity sang DTO
    public static ChatHistoryResponse fromMessage(com.example.demo.entity.chat_bot.Message message) {
        return ChatHistoryResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .direction(message.getDirection())
                .timestamp(message.getCreatedAt())
                .tokensUsed(message.getTokensUsed())
                .build();
    }
}