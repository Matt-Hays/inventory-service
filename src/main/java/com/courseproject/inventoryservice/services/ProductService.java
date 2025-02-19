package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws EntityNotFoundException {
        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product)
            throws EntityNotFoundException, OptimisticLockingFailureException {
        Product oldProduct = getProductById(id);
        if (product.getName() != null) oldProduct.setName(product.getName());
        if (product.getDescription() != null) oldProduct.setDescription(product.getDescription());
        if (product.getPrice() != null) oldProduct.setPrice(product.getPrice());
        if (product.getQuantity() != null) oldProduct.setQuantity(product.getQuantity());
        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product deductProductQuantity(Long id, Double reductionAmount)
            throws EntityNotFoundException, IllegalArgumentException, OptimisticLockingFailureException {
        Product oldProduct = getProductById(id);
        Double oldQuantity = oldProduct.getQuantity();
        if (oldQuantity - reductionAmount > 0) oldProduct.setQuantity(oldQuantity - reductionAmount);
        else throw new IllegalArgumentException("Product cannot be reduced below zero.");
        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product addProductQuantity(Long id, Double quantity)
            throws EntityNotFoundException, IllegalArgumentException, OptimisticLockingFailureException {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity cannot be zero or less than zero.");
        Product oldProduct = getProductById(id);
        oldProduct.setQuantity(oldProduct.getQuantity() + quantity);
        return productRepository.save(oldProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
