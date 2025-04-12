package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.source.SimpleSourceBean;
import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    private final RedisTemplate<Long, Product> redisTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws EntityNotFoundException {
        Product product = redisTemplate.opsForValue().get(id);
        if (product == null) {
            log.info("Product " + id + " not found in Redis");
            product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            redisTemplate.opsForValue().set(id, product);
        }

        simpleSourceBean.publishProductChange("GET", id);
        return product;
    }

    public Product saveProduct(Product product) {
        Product newProduct = productRepository.save(product);
        simpleSourceBean.publishProductChange("CREATE", newProduct.getId());
        return newProduct;
    }

    @Transactional
    public Product updateProduct(Long id, Product product)
            throws EntityNotFoundException, OptimisticLockingFailureException {
        Product oldProduct = getProductById(id);
        if (product.getName() != null)
            oldProduct.setName(product.getName());
        if (product.getDescription() != null)
            oldProduct.setDescription(product.getDescription());
        if (product.getPrice() != null)
            oldProduct.setPrice(product.getPrice());
        if (product.getQuantity() != null)
            oldProduct.setQuantity(product.getQuantity());

        simpleSourceBean.publishProductChange("UPDATE", id);
        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product deductProductQuantity(Long id, Double reductionAmount)
            throws EntityNotFoundException, IllegalArgumentException, OptimisticLockingFailureException {
        Product oldProduct = getProductById(id);
        Double oldQuantity = oldProduct.getQuantity();
        if (oldQuantity - reductionAmount > 0)
            oldProduct.setQuantity(oldQuantity - reductionAmount);
        else
            throw new IllegalArgumentException("Product cannot be reduced below zero.");

        simpleSourceBean.publishProductChange("UPDATE", id);
        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product addProductQuantity(Long id, Double quantity)
            throws EntityNotFoundException, IllegalArgumentException, OptimisticLockingFailureException {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity cannot be zero or less than zero.");
        Product oldProduct = getProductById(id);
        oldProduct.setQuantity(oldProduct.getQuantity() + quantity);
        simpleSourceBean.publishProductChange("UPDATE", id);
        return productRepository.save(oldProduct);
    }

    public void deleteProduct(Long id) {
        simpleSourceBean.publishProductChange("DELETE", id);
        productRepository.deleteById(id);
    }
}
