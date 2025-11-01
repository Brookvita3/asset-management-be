package com.example.demo.dto.asset;

import java.math.BigDecimal;
import java.sql.Date;

import com.example.demo.entity.Asset;
import com.example.demo.enums.AssetCondition;
import com.example.demo.enums.AssetStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequest {
    @NotBlank(message = "Asset code is required")
    private String code;

    @NotBlank(message = "Asset name is required")
    private String name;

    @NotNull(message = "Asset type is required")
    @Positive(message = "Asset type must be a positive id")
    private Long typeId;

    private Long assignedTo;

    @PastOrPresent(message = "Purchase date cannot be in the future")
    private Date purchaseDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Asset value must be greater than zero")
    private BigDecimal value;

    @NotNull(message = "Asset status is required")
    private AssetStatus status;

    @NotNull(message = "Asset condition is required")
    private AssetCondition condition;

    private String description;
}
