package com.example.demo.controller.chat_bot;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.chat_bot.ChatbotRequest;
import com.example.demo.dto.chat_bot.ChatbotResponse;
import com.example.demo.dto.chat_bot.ChatHistoryResponse;
import com.example.demo.service.chat_bot.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chatbot API", description = "API để giao tiếp với chatbot hỗ trợ hệ thống")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "Chat với trợ lý AI", description = "Gửi tin nhắn đến chatbot và nhận câu trả lời thông minh")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = ResponseObject.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PostMapping("/chat")
    public ResponseEntity<ResponseObject> chatWithBot(
            @Valid @RequestBody ChatbotRequest request,
            @Parameter(description = "ID của người dùng", required = true)
            @RequestParam Long userId) {

        try {
            // Gọi service xử lý - service sẽ tự lấy user và role
            ChatbotResponse response = chatbotService.chatWithBot(request, userId);

            if (response.isSuccess()) {
                return ResponseEntity.ok(
                    ResponseObject.builder()
                        .message("Chat thành công")
                        .data(response)
                        .build()
                );
            } else {
                return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                        .message(response.getMessage())
                        .data(null)
                        .build()
                    );
            }

        } catch (RuntimeException e) {
            log.error("Chat error for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseObject.builder()
                    .message("Lỗi server: " + e.getMessage())
                    .data(null)
                    .build()
                );
        } catch (Exception e) {
            log.error("Internal server error during chat for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseObject.builder()
                    .message("Lỗi server: " + e.getMessage())
                    .data(null)
                    .build()
                );
        }
    }

    @Operation(summary = "Lấy lịch sử chat", description = "Lấy toàn bộ lịch sử hội thoại giữa người dùng và chatbot")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = ResponseObject.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @GetMapping("/history")
    public ResponseEntity<ResponseObject> getChatHistory(
            @Parameter(description = "ID của người dùng", required = true)
            @RequestParam Long userId) {

        try {
            List<ChatHistoryResponse> history = chatbotService.getChatHistory(userId);

            return ResponseEntity.ok(
                ResponseObject.builder()
                    .message("Lấy lịch sử chat thành công")
                    .data(history)
                    .build()
            );

        } catch (RuntimeException e) {
            log.error("Error getting chat history for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseObject.builder()
                    .message("Không tìm thấy người dùng: " + e.getMessage())
                    .data(null)
                    .build()
                );
        } catch (Exception e) {
            log.error("Internal server error getting chat history for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseObject.builder()
                    .message("Lỗi server: " + e.getMessage())
                    .data(null)
                    .build()
                );
        }
    }

    @Operation(summary = "Health check", description = "Kiểm tra trạng thái của chatbot service")
    @GetMapping("/health")
    public ResponseEntity<ResponseObject> healthCheck() {
        return ResponseEntity.ok(
            ResponseObject.builder()
                .message("Chatbot service is running")
                .data("OK")
                .build()
        );
    }
}
