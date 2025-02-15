package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.repositories.PurchaseOrderLineItemRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    PurchaseOrderLineItemRepository purchaseOrderLineItemRepository;
    @Autowired
    ProductService productService;

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        return purchaseOrder;
    }

    public PurchaseOrder getPurchaseOrderById(UUID uuid) {
        return purchaseOrderRepository.findById(uuid).orElse(null);
    }

    public PurchaseOrder addProduct(UUID purchaseOrderId, UUID productId, long quantity) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        if (null == purchaseOrder) {
            throw new RuntimeException("Purchase order not found");
        }

        Product product = productService.getProductById(productId);
        if (null == product) {
            throw new EntityNotFoundException("invalid productId");
        }

        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem();
        purchaseOrderLineItem.setProduct(product);
        purchaseOrderLineItem.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineItem.setQuantity(quantity);

        purchaseOrder.addPurchaseOrderLineItem(purchaseOrderLineItem);

        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        return purchaseOrder;
    }
}
