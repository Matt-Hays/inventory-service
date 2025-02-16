package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/product/")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getProduct(@PathVariable UUID uuid) {
        try {
            Product product = productService.getProductById(uuid);
            return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.createProduct(product);
            return savedProduct != null ? ResponseEntity.ok(savedProduct) : ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
