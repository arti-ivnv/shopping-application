package com.artiprogram.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artiprogram.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    Optional<Inventory> findBySkuCode(String skuCode);

}
