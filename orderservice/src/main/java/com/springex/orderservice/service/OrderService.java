package com.springex.orderservice.service;

import com.springex.orderservice.domain.Order;
import com.springex.orderservice.domain.OrderItem;
import com.springex.orderservice.dto.OrderItemDto;
import com.springex.orderservice.dto.OrderRequestDto;
import com.springex.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequestDto requestDto){
        Order order = new Order();
        order.setOrderSeqId(UUID.randomUUID().toString());

        List<OrderItem> items = requestDto.getOrderItems()
                .stream()
                .map(orderItemDto -> mapFromDto(orderItemDto))
                .toList();

        order.setOrderItems(items);

        //check web client
        Boolean blockingRes = webClient.get()
                .uri("http://localhost:8082/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(blockingRes) {
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Not in stock");
        }
    }

    private OrderItem mapFromDto(OrderItemDto orderItemDto) {
        OrderItem item = new OrderItem();
        item.setPrice(orderItemDto.getPrice());
        item.setQuantity(orderItemDto.getQuantity());
        item.setSkuCode(orderItemDto.getSkuCode());

        return item;
    }
}
