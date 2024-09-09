package com.grepp.coffee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class OrderItemResponseDTO {
    // 해당 주문에서
    private String productName;
    private Long price;
    private Integer quantity;

}
