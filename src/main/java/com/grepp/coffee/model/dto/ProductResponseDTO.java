package com.grepp.coffee.model.dto;

import com.grepp.coffee.model.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductResponseDTO {
    private String name;
    private ProductCategory category;
    private Long price;
    private String description;


}
