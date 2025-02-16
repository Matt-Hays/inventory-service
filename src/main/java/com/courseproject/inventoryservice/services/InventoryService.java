package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
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
        Integer poCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM purchase_order WHERE id = ?",
                Integer.class,
                purchaseOrderId
        );
        if (poCount == null || poCount == 0) {
            throw new IllegalArgumentException("Purchase order not found: " + purchaseOrderId);
        }

        String sqlLineItems = """
                    SELECT id, quantity, product_id
                      FROM purchase_order_line_item
                     WHERE purchase_order_id = ?
                """;
        List<Map<String, Object>> lineItems = jdbcTemplate.queryForList(sqlLineItems, purchaseOrderId);

        if (lineItems.isEmpty()) {
            return Collections.emptyList();
        }

        Set<UUID> updatedProductIds = new HashSet<>();

        jdbcTemplate.execute("BEGIN;");

        try {
            for (Map<String, Object> row : lineItems) {
                Double lineItemQty = (Double) row.get("quantity");
                UUID productId = (UUID) row.get("product_id");

                String productSelect = """
                            SELECT id, version, name, description, price, quantity
                              FROM product
                             WHERE id = ?
                        """;
                Product p = jdbcTemplate.queryForObject(
                        productSelect,
                        new BeanPropertyRowMapper<>(Product.class),
                        productId
                );

                if (p == null) {
                    throw new IllegalStateException("Product not found: " + productId);
                }

                Double newQty = p.getQuantity() + lineItemQty;

                jdbcTemplate.update(
                        "UPDATE product SET quantity = ? WHERE id = ?",
                        newQty, productId
                );

                updatedProductIds.add(productId);
            }

            jdbcTemplate.execute("COMMIT;");

        } catch (Exception e) {
            jdbcTemplate.execute("ROLLBACK;");
            throw e;
        }

        if (updatedProductIds.isEmpty()) {
            return Collections.emptyList();
        }

        String inClause = updatedProductIds.stream()
                .map(id -> "'" + id.toString() + "'")
                .collect(Collectors.joining(", "));

        String finalSelect = String.format("""
                    SELECT id, version, name, description, price, quantity
                      FROM product
                     WHERE id IN (%s)
                """, inClause);

        return jdbcTemplate.query(finalSelect, new BeanPropertyRowMapper<>(Product.class));
    }

//        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
//        purchaseOrder.getPurchaseOrderLineItems()
//                .forEach(purchaseOrderLineItem -> {
//                    Double addedQty = purchaseOrderLineItem.getProduct().getQuantity();
//                    Product product = purchaseOrderLineItem.getProduct();
//                    System.out.println("Yello");
//                    try {
//                        jdbcTemplate.execute("BEGIN;");
//                        Product p = jdbcTemplate.queryForObject("SELECT * FROM product WHERE id = ?", Product.class, product.getId());
//                        Double qty = p.getQuantity();
//                        assert qty != null;
//                        jdbcTemplate.update("UPDATE product SET quantity = ? WHERE id = ?", addedQty + qty, product.getId());
//                        jdbcTemplate.execute("COMMIT;");
//                    } catch (Exception e) {
//                        jdbcTemplate.execute("ROLLBACK;");
//                        throw e;
//                    }
//                });
//        return productRepository.findAllById(
//                purchaseOrder.getPurchaseOrderLineItems()
//                        .stream()
//                        .map(PurchaseOrderLineItem::getProduct)
//                        .collect(Collectors.toSet())
//                        .stream()
//                        .map(Product::getId)
//                        .collect(Collectors.toSet())
//        );
//    }
}
