package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AssetType;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {
    Optional<AssetType> findByName(String name);
}
