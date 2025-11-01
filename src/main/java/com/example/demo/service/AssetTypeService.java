package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.assettype.AssetTypeRequest;
import com.example.demo.dto.assettype.AssetTypeResponse;
import com.example.demo.entity.AssetType;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.AssetTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetTypeService {
    private final AssetTypeRepository assetTypeRepository;

    public void create(AssetTypeRequest request) {
        AssetType assetType = AssetType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();
        assetTypeRepository.save(assetType);
    }

    public void update(Long id, AssetTypeRequest request) throws DataNotFound {
        AssetType assetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Asset type not found"));
        assetType.setName(request.getName());
        assetType.setDescription(request.getDescription());
        assetType.setIsActive(request.getIsActive());
        assetTypeRepository.save(assetType);
    }

    public void delete(Long id) {
        var assetType = assetTypeRepository.findById(id).orElseThrow();
        assetTypeRepository.delete(assetType);
    }

    public AssetTypeResponse getById(Long id) throws DataNotFound {
        return assetTypeRepository.findById(id)
                .map(assetType -> new AssetTypeResponse(assetType.getId(), assetType.getName(),
                        assetType.getDescription(),
                        assetType.getIsActive()))
                .orElseThrow(() -> new DataNotFound("Asset type not found"));
    }

    public List<AssetTypeResponse> getAll() {
        return assetTypeRepository.findAll()
                .stream()
                .map(assetType -> new AssetTypeResponse(assetType.getId(), assetType.getName(),
                        assetType.getDescription(),
                        assetType.getIsActive()))
                .toList();
    }

}
