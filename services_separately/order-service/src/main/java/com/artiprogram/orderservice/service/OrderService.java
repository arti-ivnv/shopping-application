package com.artiprogram.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
