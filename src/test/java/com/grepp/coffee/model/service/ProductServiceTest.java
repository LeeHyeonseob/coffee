package com.grepp.coffee.model.service;

import com.grepp.coffee.model.dto.ProductRequestDTO;
import com.grepp.coffee.model.dto.ProductResponseDTO;
import com.grepp.coffee.model.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private ProductRequestDTO sampleProductRequestDTO;
    private ProductResponseDTO createdProduct;

    @BeforeEach
    void setUp() {
        sampleProductRequestDTO = new ProductRequestDTO();
        sampleProductRequestDTO.setName("Test Coffee");
        sampleProductRequestDTO.setPrice(1000L);
        sampleProductRequestDTO.setCategory(ProductCategory.ICE);
        sampleProductRequestDTO.setDescription("Test coffee description");

        createdProduct = productService.createProduct(sampleProductRequestDTO);
    }

    @Test
    void getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();

        assertThat(products).isNotEmpty();
        assertThat(products).anyMatch(p -> p.getName().equals("Test Coffee"));
    }

    @Test
    void getProductByName() {
        ProductResponseDTO foundProduct = productService.getProductByName("Test Coffee");

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("Test Coffee");
        assertThat(foundProduct.getPrice()).isEqualTo(1000L);
        assertThat(foundProduct.getCategory()).isEqualTo(ProductCategory.ICE);
        assertThat(foundProduct.getDescription()).isEqualTo("Test coffee description");
    }

    @Test
    void createProduct() {
        ProductRequestDTO newProductDTO = new ProductRequestDTO();
        newProductDTO.setName("New Coffee");
        newProductDTO.setPrice(1500L);
        newProductDTO.setCategory(ProductCategory.ICE);
        newProductDTO.setDescription("New coffee description");

        ProductResponseDTO newProduct = productService.createProduct(newProductDTO);

        //동일한지 check

        assertThat(newProduct).isNotNull();
        assertThat(newProduct.getName()).isEqualTo("New Coffee");
        assertThat(newProduct.getPrice()).isEqualTo(1500L);
        assertThat(newProduct.getCategory()).isEqualTo(ProductCategory.ICE);
        assertThat(newProduct.getDescription()).isEqualTo("New coffee description");
    }

    @Test
    void updateProduct() {
        ProductRequestDTO updateDTO = new ProductRequestDTO();
        updateDTO.setName("Updated Coffee");
        updateDTO.setPrice(1200L);
        updateDTO.setCategory(ProductCategory.ICE);
        updateDTO.setDescription("Updated coffee description");

        ProductResponseDTO updatedProduct = productService.updateProduct("Test Coffee", updateDTO);

        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo("Updated Coffee");
        assertThat(updatedProduct.getPrice()).isEqualTo(1200L);
        assertThat(updatedProduct.getCategory()).isEqualTo(ProductCategory.ICE);
        assertThat(updatedProduct.getDescription()).isEqualTo("Updated coffee description");
    }

    @Test
    void deleteProduct() {
        productService.deleteProduct("Test Coffee");

        assertThatThrownBy(() -> productService.getProductByName("Test Coffee"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getProductByName_notFound() {
        String nonExistentName = "내맘대로 테스트 커피";

        assertThatThrownBy(() -> productService.getProductByName(nonExistentName))
                .isInstanceOf(RuntimeException.class);
    }
}