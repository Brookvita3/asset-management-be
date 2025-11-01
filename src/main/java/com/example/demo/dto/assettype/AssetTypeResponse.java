package com.example.demo.dto.assettype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetTypeResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
}
