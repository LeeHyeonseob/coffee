package com.grepp.coffee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    // 서비스 내에서 돌 orderitem 정보
    private String productName;
    private Integer quantity;
    private Long price;
}
