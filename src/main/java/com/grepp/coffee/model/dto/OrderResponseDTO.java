package com.grepp.coffee.model.dto;

import com.grepp.coffee.model.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    //오더 끝나고 receipt에 들어갈 내용 주기
    private UUID orderId;
    private String email;
    private String address;
    private String postcode;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> orderItems;
    private Long totalPrice; // orderItems에 있지만 총 가격도 보여주는게 나음
}
