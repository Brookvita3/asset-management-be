package com.example.demo.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetHistoryResponse {
    private Long id;
    private Long assetId;
    private String actionType;
    private Long performedBy;
    private String performedAt;
    private String details;
    private String notes;
    private String previousStatus;
    private String newStatus;
}
