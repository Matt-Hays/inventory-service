package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class InventoryService {
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public Product sellProduct(UUID id, Double qty) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        double newQty = product.getQuantity() - qty;
        if (newQty < 0) throw new IllegalArgumentException(
                "Insufficient stock. Current: " + product.getQuantity() + ", Requested: " + qty
        );

        product.setQuantity(newQty);

        return productRepository.save(product);
    }


    public List<Product> receivePurchaseOrder(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found"));

        if (purchaseOrder.getPurchaseOrderLineItems().isEmpty())
            return Collections.emptyList();

        Set<Product> updatedProducts = new HashSet<>();
        for (PurchaseOrderLineItem lineItem : purchaseOrder.getPurchaseOrderLineItems()) {
            Product product = lineItem.getProduct();
            if (product == null)
                throw new IllegalArgumentException("Line item product is null: " + lineItem.getId());

            Double lineItemQty = lineItem.getQuantity();
            Double newQty = product.getQuantity() + lineItemQty;
            product.setQuantity(newQty);

            productRepository.save(product);
            updatedProducts.add(product);
        }

        return new ArrayList<>(updatedProducts);
    }
}
