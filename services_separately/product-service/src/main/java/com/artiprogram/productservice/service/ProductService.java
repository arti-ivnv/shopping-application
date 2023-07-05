package com.artiprogram.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.artiprogram.productservice.dto.ProductRequest;
import com.artiprogram.productservice.dto.ProductResponse;
import com.artiprogram.productservice.model.Product;
import com.artiprogram.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
// helps to achieve constructor injection by automatically createing constructors
@RequiredArgsConstructor
// Adding logs
@Slf4j
public class ProductService {

    // put the object to the DB
    // using constructor injection
    private final ProductRepository productRepository;


    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                            .name(productRequest.getName())
                            .description(productRequest.getDescription())
                            .price(productRequest.getPrice())
                            .build();

        productRepository.save(product);
        log.info("Product {} is safed", product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        
        // products.stream().map(product -> mapToProductResponse(product)).toList();
        // Same as commented one
        return products.stream().map(this::mapToProductResponse).toList();


    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .build();
    }
}
