package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AssetHistory;

public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

}

