package com.grepp.coffee.model.entity;

import com.grepp.coffee.model.dto.OrderResponseDTO;
import com.grepp.coffee.model.service.OrderService;
import com.grepp.coffee.model.service.ProductService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {


    @Id
    @UuidGenerator
    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 200)
    private String postcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status",nullable = false, length = 50)
    private OrderStatus orderStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static OrderResponseDTO toDTO(Order order, Long totalPrice){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setEmail(order.getEmail());
        dto.setAddress(order.getAddress());
        dto.setPostcode(order.getPostcode());
        dto.setStatus(order.getOrderStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderItems(order.getOrderItems().stream().map(OrderItem::toDTO).collect(Collectors.toList()));
        dto.setTotalPrice(totalPrice);

        return dto;
    }
}
