package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseObject {
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;
}
