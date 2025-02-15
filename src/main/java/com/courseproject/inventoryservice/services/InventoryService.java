package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryService {
    ProductRepository productRepository;
    JdbcTemplate jdbcTemplate;

//    public Product sellProduct(UUID id, Double qty) {
//        Product product = productRepository.findById(id).orElse(null);
//        if (product == null) return null;
//        Double newQty = product.getQuantity() - qty;
//
//        try {
//            jdbcTemplate.execute("BEGIN;");
//            jdbcTemplate.update("UPDATE products SET quantity = ? WHERE id = ?", newQty, id);
//            jdbcTemplate.execute("COMMIT;");
//            return productRepository.findById(id).orElse(null);
//        } catch (Exception e) {
//            jdbcTemplate.execute("ROLLBACK;");
//            throw e;
//        }
//    }
}
