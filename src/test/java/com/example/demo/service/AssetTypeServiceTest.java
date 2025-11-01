package com.example.demo.service;

import com.example.demo.dto.assettype.AssetTypeRequest;
import com.example.demo.dto.assettype.AssetTypeResponse;
import com.example.demo.entity.AssetType;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.AssetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetTypeServiceTest {
    @Mock
    private AssetTypeRepository assetTypeRepository;

    @InjectMocks
    private AssetTypeService assetTypeService;

    private AssetTypeRequest validRequest;
    private AssetType validAssetType;
    private AssetTypeResponse validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new AssetTypeRequest("Asset 1", "Description of asset", true);
        validAssetType = new AssetType(1L, "Asset 1", "Description of asset", true, Instant.now());
        validResponse = new AssetTypeResponse(1L, "Asset 1", "Description of asset", true);
    }

    @Test
    void testCreate() {
        when(assetTypeRepository.save(any(AssetType.class))).thenReturn(validAssetType);

        assetTypeService.create(validRequest);

        verify(assetTypeRepository, times(1)).save(any(AssetType.class));
    }

    @Test
    void testUpdate() throws DataNotFound {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.of(validAssetType));
        when(assetTypeRepository.save(any(AssetType.class))).thenReturn(validAssetType);

        assetTypeService.update(1L, validRequest);

        verify(assetTypeRepository, times(1)).save(any(AssetType.class));
    }

    @Test
    void testUpdateAssetTypeNotFound() {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.empty());

        DataNotFound exception = assertThrows(DataNotFound.class, () -> {
            assetTypeService.update(1L, validRequest);
        });

        assertEquals("Asset type not found", exception.getMessage());
    }

    @Test
    void testDelete() {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.of(validAssetType));

        assetTypeService.delete(1L);

        verify(assetTypeRepository, times(1)).delete(validAssetType);
    }

    @Test
    void testDeleteAssetTypeNotFound() {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> assetTypeService.delete(1L));
    }

    @Test
    void testGetById() throws DataNotFound {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.of(validAssetType));

        AssetTypeResponse response = assetTypeService.getById(1L);

        assertNotNull(response);
        assertEquals(validResponse, response);
    }

    @Test
    void testGetByIdNotFound() {
        when(assetTypeRepository.findById(1L)).thenReturn(Optional.empty());

        DataNotFound exception = assertThrows(DataNotFound.class, () -> {
            assetTypeService.getById(1L);
        });

        assertEquals("Asset type not found", exception.getMessage());
    }

    @Test
    void testGetAll() {
        when(assetTypeRepository.findAll()).thenReturn(List.of(validAssetType));

        var response = assetTypeService.getAll();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(validResponse, response.get(0));
    }
}


