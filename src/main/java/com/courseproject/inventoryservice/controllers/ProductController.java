package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Product createProduct(@RequestBody @Valid Product product) {
        return productService.saveProduct(product);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/add/{qty}")
    public ResponseEntity<Product> addQuantityToProduct(@PathVariable Long id, @PathVariable Double qty) {
        try {
            return ResponseEntity.ok(productService.addProductQuantity(id, qty));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deduct/{qty}")
    public ResponseEntity<Product> deductQuantityFromProduct(@PathVariable Long id, @PathVariable Double qty) {
        try {
            return ResponseEntity.ok(productService.deductProductQuantity(id, qty));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/sale/{qty}")
    public ResponseEntity<Product> saleProduct(@PathVariable Long id, @PathVariable Double qty) {
        try {
            return ResponseEntity.ok(productService.deductProductQuantity(id, qty));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
