package com.grepp.coffee.model.service;

import com.grepp.coffee.model.dto.OrderItemDTO;
import com.grepp.coffee.model.dto.OrderRequestDTO;
import com.grepp.coffee.model.dto.OrderResponseDTO;
import com.grepp.coffee.model.entity.Order;
import com.grepp.coffee.model.entity.OrderItem;
import com.grepp.coffee.model.entity.OrderStatus;
import com.grepp.coffee.model.entity.Product;
import com.grepp.coffee.model.repository.OrderRepository;
import com.grepp.coffee.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final Clock clock; // LocalDateTime 시간 설정 가능하도록


    //주문 만들때 orderRequestDTO 받아서 안에 있는 처리(근데 주문 할 때, 재고 없으면 거부하기)
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = orderRequestDTO.toEntity();
        List<OrderItem> orderItems = new ArrayList<>();

        Long totalPrice = 0L;


        //상품 찾은거 포함해서 넣고 반복문 돌면서
        for (OrderItemDTO item : orderRequestDTO.getItems()) {
            Product product = productRepository.findByName(item.getProductName());//바로 받아서 레포지토리에 이름으로 찾는거 만들기

            //재고 없으면 error <- 이거는 나중에 수정할 때 Product에 stock 컬럼 추가해서 비교하는 형태로 구현


            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setCategory(product.getCategory());

            orderItems.add(orderItem);
            totalPrice += item.getQuantity() * item.getPrice();
        }

        order.setOrderItems(orderItems);
        Order saveOrder = orderRepository.save(order);

        return Order.toDTO(saveOrder, totalPrice);


    }

    public OrderResponseDTO getOrderById(UUID orderID) {
        Order order = orderRepository.findById(orderID).orElse(null);
        OrderResponseDTO dto = Order.toDTO(order, calTotalPrice(order));
        return dto;
    }

    public OrderResponseDTO updateOrder(UUID orderID, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(orderID).orElse(null);

        order.setEmail(orderRequestDTO.getEmail());
        order.setAddress(orderRequestDTO.getAddress());
        order.setPostcode(orderRequestDTO.getPostcode());

        List<OrderItem> newOrderItems = new ArrayList<>();
        Long totalPrice = 0L;

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findByName(itemDTO.getProductName());
            if (product == null) {
                throw new RuntimeException("상품을 찾을 수 없습니다: " + itemDTO.getProductName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setCategory(product.getCategory());

            newOrderItems.add(orderItem);
            totalPrice += itemDTO.getQuantity() * product.getPrice();
        }

        order.setOrderItems(newOrderItems);
        order.setUpdatedAt(LocalDateTime.now());

        Order saveOrder = orderRepository.save(order);
        return Order.toDTO(saveOrder, totalPrice);
    }

    //고객이 자신의 주문 내역 볼 수 있게
    public List<OrderResponseDTO> getOrdersByEmail(String email) {
        List<Order> orders = orderRepository.findByEmail(email);
        //totalPrice 칼럼이 따로 없어서 그냥 매번 계산해야함.... 나중에 수정단계에서 고칠 것
        return orders.stream().map(o -> Order.toDTO(o, calTotalPrice(o))).collect(Collectors.toList());
    }

    //TotalPrice계산
    public Long calTotalPrice(Order order) {
        return order.getOrderItems().stream().mapToLong(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order.getOrderStatus() != OrderStatus.대기중) {
            throw new RuntimeException("대기중인 상태에서만 주문 취소가 가능합니다.");
        }

        order.setOrderStatus(OrderStatus.취소됨);

        //나중에는 여기에 반복문 돌면서 stock 원상복구해야함..

    }

    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        order.setOrderStatus(status);
        return Order.toDTO(order, calTotalPrice(order));

    }

    //2시까지 한거
    public List<OrderResponseDTO> getDailyOrders(){
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime cutOff = now.withHour(14).withMinute(0).withSecond(0);

        LocalDateTime start, end;

        if (now.isAfter(cutOff)) { // 오후 2시 넘었으면 시작-> 그날 2시 끝 -> 다음날 2시
            start = cutOff;
            end = cutOff.plusDays(1);
        }else{
            start = cutOff.minusDays(1);
            end = cutOff;
        }

        List<Order> dailyOrders = orderRepository.findByCreatedAtBetween(start, end);

        return dailyOrders.stream().map(o -> Order.toDTO(o, calTotalPrice(o))).collect(Collectors.toList());
    }


}
