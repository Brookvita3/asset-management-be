package com.example.demo.service.chat_bot;

import com.example.demo.dto.chat_bot.ChatbotRequest;
import com.example.demo.dto.chat_bot.ChatbotResponse;
import com.example.demo.dto.chat_bot.ChatHistoryResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.chat_bot.Message;
import com.example.demo.entity.chat_bot.MessageDirection;
import com.example.demo.enums.Role;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatbotService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${zhipu.api.key}")
    private String zhipuApiKey;

    @Value("${zhipu.api.url:https://api.z.ai/api/paas/v4/chat/completions}")
    private String zhipuApiUrl;

    // Đọc tài liệu hệ thống từ file resources theo role
    private String getSystemDocumentation(Role role) {
        try {
            String fileName = switch (role) {
                case ADMIN -> "admin_documentation.txt";
                case MANAGER -> "manager_documentation.txt";
                case STAFF -> "staff_documentation.txt";
            };

            var resource = getClass().getClassLoader().getResourceAsStream(fileName);
            if (resource != null) {
                return new String(resource.readAllBytes());
            }
        } catch (IOException e) {
            log.error("Error reading {} documentation", role.name().toLowerCase(), e);
        }
        return String.format(
                "Bạn là một trợ lý AI hữu ích, chuyên hỗ trợ người dùng %s sử dụng hệ thống quản lý tài sản.",
                getRoleDisplayName(role));
    }

    // Lấy tên hiển thị của role
    private String getRoleDisplayName(Role role) {
        return switch (role) {
            case ADMIN -> "Quản trị viên (Admin)";
            case MANAGER -> "Trưởng phòng (Manager)";
            case STAFF -> "Nhân viên (Staff)";
        };
    }

    private String buildContextPrompt(Long userId) {
        // Lấy 10 tin nhắn gần nhất của user (đã sắp xếp theo createdAt DESC)
        List<Message> recentMessages = messageRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("NGỮ CẢNH HỘI THOẠI TRƯỚC ĐÓ (chỉ sử dụng nếu liên quan):\n");

        // Đảo ngược danh sách để hiển thị theo thứ tự thời gian (từ cũ nhất đến mới
        // nhất)
        // Sử dụng cách tiếp cận tương thích Java 17
        for (int i = recentMessages.size() - 1; i >= 0; i--) {
            Message msg = recentMessages.get(i);
            String role = msg.getDirection() == MessageDirection.QUESTION ? "User" : "Assistant";
            contextBuilder.append(role).append(": ").append(msg.getContent()).append("\n");
        }

        return contextBuilder.toString();
    }

    private String buildSystemPrompt(Role role) {
        String systemDoc = getSystemDocumentation(role);
        String roleDisplayName = getRoleDisplayName(role);

        return String.format(
                """
                        Bạn là một trợ lý AI thông minh và thân thiện, chuyên hỗ trợ %s sử dụng hệ thống quản lý tài sản.

                        TÀI LIỆU HỆ THỐNG DÀNH CHO %s:
                        %s

                        HƯỚNG DẪN TRẢLỜI:
                        1. Luôn trả lời một cách thân thiện và chuyên nghiệp với vai trò %s
                        2. Sử dụng tài liệu hệ thống ở trên để cung cấp thông tin chính xác
                        3. Nếu cần, sử dụng ngữ cảnh hội thoại trước đó (chỉ khi liên quan)
                        4. Hướng dẫn chi tiết từng bước cho người dùng
                        5. Nếu không tìm thấy thông tin trong tài liệu, hãy đề xuất liên hệ hỗ trợ
                        6. Trả lời bằng tiếng Việt
                        7. Chỉ cung cấp thông tin và hướng dẫn phù hợp với quyền hạn của %s
                        8. Với câu hỏi đơn giản thì đừng trả lời quá dài dòng, hãy ngắn gọn và súc tích.
                        """,
                roleDisplayName, roleDisplayName.toUpperCase(), systemDoc, roleDisplayName, roleDisplayName);
    }

    @Transactional
    public ChatbotResponse chatWithBot(ChatbotRequest request, Long userId) {
        try {
            // Lấy thông tin user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Lưu tin nhắn của user
            Message userMessage = Message.builder()
                    .user(user)
                    .content(request.getMessage())
                    .direction(MessageDirection.QUESTION)
                    .createdAt(Instant.now())
                    .build();
            messageRepository.save(userMessage);

            // Gọi AI API với role của user
            String aiResponse = callZhipuAI(request.getMessage(), userId, user.getRole());

            // Lưu câu trả lời của AI
            Message botMessage = Message.builder()
                    .user(user)
                    .content(aiResponse)
                    .direction(MessageDirection.ANSWER)
                    .createdAt(Instant.now())
                    .build();
            messageRepository.save(botMessage);

            return ChatbotResponse.success(aiResponse);

        } catch (Exception e) {
            log.error("Error in chatbot service", e);
            return ChatbotResponse.error("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    // Lấy lịch sử chat của người dùng
    public List<ChatHistoryResponse> getChatHistory(Long userId) {
        try {
            // Kiểm tra xem user có tồn tại không
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Lấy tất cả tin nhắn của user, sắp xếp theo thời gian tăng dần
            List<Message> messages = messageRepository.findRecentMessagesByUserId(userId);

            // Chuyển đổi sang DTO
            return messages.stream()
                    .map(ChatHistoryResponse::fromMessage)
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            log.error("Error getting chat history for user {}: {}", userId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting chat history for user {}", userId, e);
            throw new RuntimeException("Có lỗi xảy ra khi lấy lịch sử chat: " + e.getMessage());
        }
    }

    private String callZhipuAI(String userMessage, Long userId, Role userRole)
            throws IOException, InterruptedException {
        // Build context from recent messages
        String contextPrompt = buildContextPrompt(userId);

        // Create request payload
        String requestBody = String.format("""
                {
                    "model": "glm-4.5-airx",
                    "messages": [
                        {
                            "role": "system",
                            "content": "%s"
                        },
                        {
                            "role": "system",
                            "content": "%s"
                        },
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "temperature": 0.7,
                    "stream": false
                }
                """,
                escapeJsonString(buildSystemPrompt(userRole)),
                escapeJsonString(contextPrompt),
                escapeJsonString(userMessage));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(zhipuApiUrl))
                .header("Content-Type", "application/json")
                .header("Accept-Language", "en-US,en")
                .header("Authorization", "Bearer " + zhipuApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "AI API call failed with status: " + response.statusCode() + ", body: " + response.body());
        }

        // Parse response
        JsonNode responseJson = objectMapper.readTree(response.body());
        JsonNode choices = responseJson.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode message = choices.get(0).get("message");
            if (message != null) {
                JsonNode content = message.get("content");
                if (content != null) {
                    return content.asText();
                }
            }
        }

        throw new RuntimeException("Invalid response format from AI API");
    }

    private String escapeJsonString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
