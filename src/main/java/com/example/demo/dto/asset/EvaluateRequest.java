package com.example.demo.dto.asset;

import com.example.demo.enums.AssetCondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRequest {
    private Long performedBy;
    private String details;
    private String notes;
    private String previousStatus;
    private String newStatus;
    private AssetCondition condition;
}
