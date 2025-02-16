package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryService {
    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public Product sellProduct(UUID id, Double qty) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return null;

        Double newQty = product.getQuantity() - qty;

        try {
            jdbcTemplate.execute("BEGIN;");
            jdbcTemplate.update("UPDATE product SET quantity = ? WHERE id = ?", newQty, id);
            jdbcTemplate.execute("COMMIT;");
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            jdbcTemplate.execute("ROLLBACK;");
            throw e;
        }
    }


    public List<Product> receivePurchaseOrder(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
        purchaseOrder.getPurchaseOrderLineItems()
                .forEach(purchaseOrderLineItem -> {
                    Double addedQty = purchaseOrderLineItem.getProduct().getQuantity();
                    Product product = purchaseOrderLineItem.getProduct();
                    try {
                        jdbcTemplate.execute("BEGIN;");
                        Product p = jdbcTemplate.queryForObject("SELECT product WHERE id = ?", Product.class, product.getId());
                        Double qty = p.getQuantity();
                        jdbcTemplate.update("UPDATE product SET quantity = ? WHERE id = ?", addedQty + qty, product.getId());
                        jdbcTemplate.execute("COMMIT;");
                    } catch (Exception e) {
                        jdbcTemplate.execute("ROLLBACK;");
                        throw e;
                    }
                });
        return productRepository.findAllById(
                purchaseOrder.getPurchaseOrderLineItems()
                        .stream()
                        .map(PurchaseOrderLineItem::getProduct)
                        .collect(Collectors.toSet())
                        .stream()
                        .map(Product::getId)
                        .collect(Collectors.toSet())
        );
    }
}
