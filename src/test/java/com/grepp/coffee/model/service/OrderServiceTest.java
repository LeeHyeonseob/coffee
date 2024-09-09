package com.grepp.coffee.model.service;

import com.grepp.coffee.model.dto.OrderItemDTO;
import com.grepp.coffee.model.dto.OrderRequestDTO;
import com.grepp.coffee.model.dto.OrderResponseDTO;
import com.grepp.coffee.model.dto.ProductRequestDTO;
import com.grepp.coffee.model.entity.OrderStatus;
import com.grepp.coffee.model.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    private OrderRequestDTO sampleOrderRequestDTO;

    @BeforeEach
    void setUp() {
        // Create a test product
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("Test Coffee");
        productRequestDTO.setPrice(1000L);
        productRequestDTO.setCategory(ProductCategory.HOT);
        productRequestDTO.setDescription("Test coffee description");
        productService.createProduct(productRequestDTO);

        // Create a sample OrderRequestDTO
        sampleOrderRequestDTO = createSampleOrderRequestDTO();
    }

    @Test
    void createAndRetrieveOrder() {
        OrderResponseDTO createdOrder = orderService.createOrder(sampleOrderRequestDTO);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderId()).isNotNull();

        OrderResponseDTO retrievedOrder = orderService.getOrderById(createdOrder.getOrderId());
        assertThat(retrievedOrder)
                .usingRecursiveComparison()
                .isEqualTo(createdOrder);
    }

    @Test
    void updateOrderStatus() {
        OrderResponseDTO createdOrder = orderService.createOrder(sampleOrderRequestDTO);

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(createdOrder.getOrderId(), OrderStatus.제조중);

        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.제조중);
    }

    @Test
    void getDailyOrders() {
        orderService.createOrder(sampleOrderRequestDTO);

        List<OrderResponseDTO> dailyOrders = orderService.getDailyOrders();

        assertThat(dailyOrders).isNotEmpty();
    }

    @Test
    void cancelOrder() {
        OrderResponseDTO createdOrder = orderService.createOrder(sampleOrderRequestDTO);

        orderService.cancelOrder(createdOrder.getOrderId());

        OrderResponseDTO cancelledOrder = orderService.getOrderById(createdOrder.getOrderId());
        assertThat(cancelledOrder.getStatus()).isEqualTo(OrderStatus.취소됨);
    }

    private OrderRequestDTO createSampleOrderRequestDTO() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setEmail("test@example.com");
        orderRequestDTO.setAddress("123 Test St");
        orderRequestDTO.setPostcode("12345");

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductName("Test Coffee");
        itemDTO.setQuantity(2);
        itemDTO.setPrice(1000L);

        orderRequestDTO.setItems(Arrays.asList(itemDTO));
        return orderRequestDTO;
    }
}