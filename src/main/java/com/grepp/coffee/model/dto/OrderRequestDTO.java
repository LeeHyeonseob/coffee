package com.grepp.coffee.model.dto;

import com.grepp.coffee.model.entity.Order;
import com.grepp.coffee.model.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequestDTO {
    private String email;
    private String address;
    private String postcode;
    private List<OrderItemDTO> items;

    public Order toEntity() {
        Order order = new Order();
        order.setEmail(this.email);
        order.setAddress(this.address);
        order.setPostcode(this.postcode);
        order.setOrderStatus(OrderStatus.대기중);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }
}
