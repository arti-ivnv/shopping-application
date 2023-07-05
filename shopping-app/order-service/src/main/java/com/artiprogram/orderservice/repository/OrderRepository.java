package com.artiprogram.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artiprogram.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
