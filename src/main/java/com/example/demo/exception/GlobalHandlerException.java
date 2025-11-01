package com.example.demo.exception;

import com.example.demo.dto.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseObject> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseObject.builder()
                        .message(ex.getMessage())
                        .data("error in server")
                        .build()
        );
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseObject> handleAuthException(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseObject.builder()
                        .message(ex.getMessage())
                        .data("Unauthorized")
                        .build()
        );
    }
}
