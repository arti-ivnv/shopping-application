package com.artiprogram.productservice.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "product") // Mongo mapping
@AllArgsConstructor
@NoArgsConstructor
@Builder // Creates package-private but not working with @XArgsConstructor
@Data // Getters and Setters
public class Product {
    @Id
    private String id; // primary key
    private String name;
    private String description;
    private BigDecimal price;

}
