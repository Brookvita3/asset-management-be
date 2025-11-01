package com.example.demo.controller.asset;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.asset.AssetRequest;
import com.example.demo.dto.asset.AssetResponse;
import com.example.demo.service.AssetService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping()
    public ResponseEntity<ResponseObject> createAsset(@RequestBody AssetRequest assetRequest) {
        assetService.create(assetRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset created successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAssetById(@PathVariable Long id) {
        AssetResponse assetResponse = assetService.getById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(assetResponse)
                .build());
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> getAllAssets() {
        List<AssetResponse> assets = assetService.getAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(assets)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAsset(@PathVariable Long id, @RequestBody AssetRequest assetRequest) {
        assetService.update(id, assetRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAsset(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset deleted successfully")
                .build());
    }
}
