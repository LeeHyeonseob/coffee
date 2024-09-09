package com.grepp.coffee.controller;

import com.grepp.coffee.model.dto.ProductRequestDTO;
import com.grepp.coffee.model.dto.ProductResponseDTO;
import com.grepp.coffee.model.entity.Product;
import com.grepp.coffee.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //커피 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    //하나 조회
//    @GetMapping("/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        ProductResponseDTO dto = productService.getProductByName(name);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
        ProductResponseDTO dto = productService.getProductById(productId);
        return ResponseEntity.ok(dto);
    }

    //커피 등록
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO dto = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(dto);
    }

    //커피 삭제
//    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteProduct(@PathVariable String name) {
        productService.deleteProduct(name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

    //커피 업데이트
//    @PutMapping("/{name}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String name, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO dto = productService.updateProduct(name, productRequestDTO);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO dto = productService.updateProductById(productId, productRequestDTO);
        return ResponseEntity.ok(dto);
    }

    /*
        추가적인 고민 -> PathVariable로 파라미터를 받을지 아니면 @RequestParam으로 받을지
        RESTful API 설계를 보면 리소스 필터링, 정렬, 페이징 시에는 RequestParam활용하고 리소스를 직접 식별하는 경우에는 PathVariable을 사용한다 => 이 경우에는 PathVariable을 사용한다
     */
}
