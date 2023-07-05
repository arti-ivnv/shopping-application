package com.artiprogram.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.artiprogram.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
