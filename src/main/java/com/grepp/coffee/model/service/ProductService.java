package com.grepp.coffee.model.service;

import com.grepp.coffee.model.dto.ProductRequestDTO;
import com.grepp.coffee.model.dto.ProductResponseDTO;
import com.grepp.coffee.model.entity.Product;
import com.grepp.coffee.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    //READ
    public List<ProductResponseDTO> getAllProducts(){
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream().map(Product::toDTO).collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(UUID id){
        Product product = productRepository.findById(id).orElse(null);
        return product.toDTO();
    }

    public ProductResponseDTO getProductByName(String name) {

        Product product = productRepository.findByName(name);
        if(product == null){
            throw new RuntimeException();
        }
        return product.toDTO();
    }

    //CREATE
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(productRequestDTO.getCategory());
        product.setCreatedAt(LocalDateTime.now());

        return product.toDTO();

    }

    //update

    public ProductResponseDTO updateProduct(String name, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findByName(name);
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(productRequestDTO.getCategory());
        product.setUpdatedAt(LocalDateTime.now());

        return product.toDTO();
    }

    public ProductResponseDTO updateProductById(UUID id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElse(null);
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(productRequestDTO.getCategory());
        product.setUpdatedAt(LocalDateTime.now());

        return product.toDTO();
    }

    //delete
    public void deleteProduct(String name){
        Product product = productRepository.findByName(name);
        if(product == null){
            throw new RuntimeException("그런 커피 없음");
        }

        productRepository.delete(product);
    }

    public void deleteProductById(UUID id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            throw new RuntimeException("그런 커피 없음");
        }

        productRepository.delete(product);
    }
}
