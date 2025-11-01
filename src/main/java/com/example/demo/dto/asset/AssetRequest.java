package com.example.demo.dto.asset;

import java.math.BigDecimal;
import java.sql.Date;

import com.example.demo.entity.Asset;
import com.example.demo.enums.AssetCondition;
import com.example.demo.enums.AssetStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequest {
    private String code;
    private String name;
    private Long typeId;
    private Long assignedTo;
    private Date purchaseDate;
    private BigDecimal value;
    private AssetStatus status;
    private AssetCondition condition;
    private String description;
}
