package com.example.demo.controller.assettype;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.assettype.AssetTypeRequest;
import com.example.demo.service.AssetTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/asset-types")
@RequiredArgsConstructor
public class AssetTypeController {

    private final AssetTypeService assetTypeService;

    @PostMapping()
    public ResponseEntity<ResponseObject> createAssetType(@RequestBody AssetTypeRequest request) {
        assetTypeService.create(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset type created successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAssetTypeById(@PathVariable Long id) {
        var assetType = assetTypeService.getById(id);
        if (assetType != null) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Asset type retrieved successfully")
                    .data(assetType)
                    .build());
        } else {
            return ResponseEntity.status(404).body(ResponseObject.builder()
                    .message("Asset type not found")
                    .build());
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllAssetTypes() {
        var assetTypes = assetTypeService.getAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset types retrieved successfully")
                .data(assetTypes)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAssetType(@PathVariable Long id,
            @RequestBody AssetTypeRequest request) {
        assetTypeService.update(id, request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset type updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAssetType(@PathVariable Long id) {
        assetTypeService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset type deleted successfully")
                .build());
    }
}
