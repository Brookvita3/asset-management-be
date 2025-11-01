package com.example.demo.controller.asset;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.asset.AssetAssignRequest;
import com.example.demo.dto.asset.AssetRequest;
import com.example.demo.dto.asset.AssetResponse;
import com.example.demo.service.AssetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping()
    public ResponseEntity<ResponseObject> createAsset(@Valid @RequestBody AssetRequest assetRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

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
    public ResponseEntity<ResponseObject> updateAsset(@PathVariable Long id,
            @Valid @RequestBody AssetRequest assetRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        assetService.update(id, assetRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset updated successfully")
                .build());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<ResponseObject> assignAsset(@PathVariable("id") Long assetId,
            @Valid @RequestBody AssetAssignRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        assetService.assign(assetId, request.getUserId());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset assigned successfully")
                .build());
    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<ResponseObject> revokeAsset(@PathVariable("id") Long assetId) {
        assetService.revoke(assetId);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset assignment revoked successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAsset(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Asset deleted successfully")
                .build());
    }

    private ResponseEntity<ResponseObject> buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ResponseObject.builder()
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }
}
