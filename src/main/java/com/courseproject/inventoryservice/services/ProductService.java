package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product replaceProductById(UUID id, Product product) {
        Product oldProduct = productRepository.findById(id).orElse(null);
        if (oldProduct == null) return null;
        product.setId(oldProduct.getId());
        return productRepository.save(product);
    }

    public Product updateProductById(UUID id, Product product) {
        Product oldProduct = productRepository.findById(id).orElse(null);
        if (oldProduct == null) return null;
        if (product.getName() != null) oldProduct.setName(product.getName());
        if (product.getDescription() != null) oldProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) oldProduct.setPrice(product.getPrice());
        return productRepository.save(oldProduct);
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }
}
