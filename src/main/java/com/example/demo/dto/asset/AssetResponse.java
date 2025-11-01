package com.example.demo.dto.asset;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;

import com.example.demo.enums.AssetCondition;
import com.example.demo.enums.AssetStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponse {
    private Long id;
    private String code;
    private String name;
    private String type;
    private String assignedTo;
    private Date purchaseDate;
    private BigDecimal value;
    private AssetStatus status;
    private AssetCondition condition;
    private String description;
    private String createdBy;
    private Instant createdAt;
}