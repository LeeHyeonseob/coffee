package com.grepp.coffee.model.repository;

import com.grepp.coffee.model.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
    List<Order> findByEmailAndCreatedAtBetween(String email, LocalDateTime start, LocalDateTime end);

    List<Order> findByEmail(String email);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :start AND o.createdAt < :end ORDER BY o.createdAt")
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
