package com.artiprogram.orderservice.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.artiprogram.orderservice.dto.InventoryResponse;
import com.artiprogram.orderservice.dto.OrderLineItemsDto;
import com.artiprogram.orderservice.dto.OrderRequest;
import com.artiprogram.orderservice.model.Order;
import com.artiprogram.orderservice.model.OrderLineItems;
import com.artiprogram.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional // Spring automatically create and commit the transactions 
public class OrderService {

    private final OrderRepository orderRepository;

    // Should be the same name as function name in the config Bean
    private final  WebClient webClient;
    
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        

        // Setting orders number
        order.setOrderNumber(UUID.randomUUID().toString());

        // orderRequest.getOrderLineItemsDtoList().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto));
        List <OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                                                            .stream()
                                                            .map(this::mapToDto)
                                                            .toList();
        order.setOrderLineItemsList(orderLineItems);

        // Get all the requested skuCodes of the order.
        // order.getOrderLineItemsList().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Call Inventory service, and place order if producr is in stock
        InventoryResponse[] inventoryResponseArray = webClient.get().uri("http://localhost:8083/api/inventory", 
                                                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                                                                .retrieve()
                                                                                // We need this to be able to read the data
                                                                                .bodyToMono(InventoryResponse[].class)
                                                                                    .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        
        if (allProductsInStock){
            orderRepository.save(order);
        } else { 
            throw new IllegalArgumentException("Product is not in stock, please try later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
