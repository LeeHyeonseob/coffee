package com.grepp.coffee.model.dto;

import com.grepp.coffee.model.entity.ProductCategory;
import lombok.Data;

@Data
public class ProductRequestDTO {

    private String name;
    private ProductCategory category;
    private Long price;
    private String description;

}
