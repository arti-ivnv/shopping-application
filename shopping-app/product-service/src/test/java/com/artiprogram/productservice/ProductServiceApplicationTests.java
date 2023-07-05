package com.artiprogram.productservice;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.artiprogram.productservice.dto.ProductRequest;
import com.artiprogram.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

// TODO: thread com.mongodb.MongoSocketReadException: Prematurely reached end of stream
// TODO: Learn more about tests

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	// Converts POJO object to JSON and vice verse
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry  dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}


	@Test
	void shouldCreateProduct() throws Exception{

		ProductRequest productRequest = getProductRequest();
		String productRequestString =  objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
												.contentType(MediaType.APPLICATION_JSON)
												.content(productRequestString))
												.andExpect(status().isCreated());

		// Verifieing if the product asserted or not
		Assertions.assertTrue(productRepository.findAll().size() == 1);
	}


	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
								.name("Iphone 13")
								.description("Wonderfull Iphone 13")
								.price(BigDecimal.valueOf(1244))
								.build();
	}

	@Test
	void shouldGetAllProducts() throws Exception{

		ProductRequest productRequest = getProductRequest();
		String productRequestString =  objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
												.contentType(MediaType.APPLICATION_JSON)
												.content(productRequestString))
												.andExpect(status().isOk());
	} 



}
