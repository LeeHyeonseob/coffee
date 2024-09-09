package com.grepp.coffee.controller;

import com.grepp.coffee.model.dto.OrderRequestDTO;
import com.grepp.coffee.model.dto.OrderResponseDTO;
import com.grepp.coffee.model.dto.ProductResponseDTO;
import com.grepp.coffee.model.entity.OrderStatus;
import com.grepp.coffee.model.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO order) {
        OrderResponseDTO response = orderService.createOrder(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam String email){
        List<OrderResponseDTO> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable UUID orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    //    @PutMapping -> 이거 나중에 stock 추가해서 수정할 때 사용할 예정 그리고 입력받을 updateDTO 따로 만들기
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable UUID orderId, @RequestBody OrderRequestDTO order) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(orderId, order);

        return ResponseEntity.ok(updatedOrder);
    }

    //status만 변경
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable UUID orderId, @RequestBody OrderStatus status) {
        OrderResponseDTO updatedStatus = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedStatus);
    }

    //2시 까지의 주문 목록 조회
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyOrders() {
        List<OrderResponseDTO> dailyOrders = orderService.getDailyOrders();
        return ResponseEntity.ok(dailyOrders);
    }



}
