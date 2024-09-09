package com.grepp.coffee.model.repository;

import com.grepp.coffee.model.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    Product findByName(String name);

}
