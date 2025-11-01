package com.example.demo.dto.asset;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignRequest {

    @NotNull(message = "User id is required")
    @Positive(message = "User id must be a positive number")
    private Long userId;
}

