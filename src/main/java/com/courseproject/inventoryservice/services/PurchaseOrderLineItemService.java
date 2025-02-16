//package com.courseproject.inventoryservice.services;
//
//import com.courseproject.inventoryservice.models.Product;
//import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
//import com.courseproject.inventoryservice.repositories.PurchaseOrderLineItemRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//
//@Service
//public class PurchaseOrderLineItemService {
//
//    @Autowired
//    PurchaseOrderLineItemRepository purchaseOrderLineItemRepository;
//    @Autowired
//    ProductService productService;
//
//    public PurchaseOrderLineItem createPurchaseOrderLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
//        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);
//
//        return purchaseOrderLineItem;
//    }
//
//    public PurchaseOrderLineItem createPurchaseOrderLineItem(UUID productId, long quantity) {
//        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem();
//        Product product = productService.getProductById(productId);
//
//        if (null == product) {
//            throw new EntityNotFoundException("invalid product id");
//        }
//
//        purchaseOrderLineItem.setProduct(product);
//        purchaseOrderLineItem.setQuantity(quantity);
//
//        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);
//
//        return purchaseOrderLineItem;
//    }
//
//    public PurchaseOrderLineItem getById(UUID purchaseOrderLineItemId) {
//        return purchaseOrderLineItemRepository.findById(purchaseOrderLineItemId).orElse(null);
//    }
//}
