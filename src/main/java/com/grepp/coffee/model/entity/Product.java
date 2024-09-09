package com.grepp.coffee.model.entity;

import com.grepp.coffee.model.dto.ProductResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "products")
public class Product {

    @Id
    @UuidGenerator
    @Column(name = "product_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "product_name", nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductCategory category;

    @Column(nullable = false)
    private Long price;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    public ProductResponseDTO toDTO(){
        return new ProductResponseDTO(
                this.name,
                this.category,
                this.price,
                this.description
        );
    }
}
