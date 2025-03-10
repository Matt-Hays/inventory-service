package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.models.enums.PurchaseOrderStatus;
import com.courseproject.inventoryservice.repositories.PurchaseOrderLineItemRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderLineItemRepository purchaseOrderLineItemRepository;
    private final ProductService productService;
    private final VendorService vendorService;

    public List<PurchaseOrder> findAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder findPurchaseOrderById(Long id) throws EntityNotFoundException {
        return purchaseOrderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        Vendor vendor = vendorService.getVendorById(purchaseOrder.getVendor().getId());
        purchaseOrder.setVendor(vendor);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder purchaseOrder) throws EntityNotFoundException, OptimisticLockingFailureException {
        PurchaseOrder oldPurchaseOrder = findPurchaseOrderById(id);
        if (purchaseOrder.getOrderDate() != null) oldPurchaseOrder.setOrderDate(purchaseOrder.getOrderDate());
        if (purchaseOrder.getDeliveryDate() != null) oldPurchaseOrder.setDeliveryDate(purchaseOrder.getDeliveryDate());
        return purchaseOrderRepository.save(oldPurchaseOrder);
    }

    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long id)
            throws EntityNotFoundException, OptimisticLockingFailureException, IllegalArgumentException {
        PurchaseOrder purchaseOrder = findPurchaseOrderById(id);
        if (purchaseOrder.getPurchaseOrderLineItems().isEmpty()) return null;
        Set<Product> updatedProducts = new HashSet<>();
        for (PurchaseOrderLineItem lineItem : purchaseOrder.getPurchaseOrderLineItems()) {
            Product product = lineItem.getProduct();
            if (product == null)
                throw new IllegalArgumentException("Line item product is null: " + lineItem.getId());

            Double lineItemQty = lineItem.getQuantity();
            Double newQty = product.getQuantity() + lineItemQty;
            product.setQuantity(newQty);

            productService.saveProduct(product);
        }
        purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public PurchaseOrder addLineItemToPurchaseOrder(Long id, List<PurchaseOrderLineItem> lineItems)
            throws EntityNotFoundException, OptimisticLockingFailureException, IllegalArgumentException {
        if (lineItems == null || lineItems.isEmpty()) throw new IllegalArgumentException("Line items is null.");
        PurchaseOrder purchaseOrder = findPurchaseOrderById(id);
        lineItems.forEach(lineItem -> {
            Product product = productService.getProductById(lineItem.getProduct().getId());
            lineItem.setProduct(product);
            lineItem.setPurchaseOrder(purchaseOrder);
        });
        purchaseOrder.getPurchaseOrderLineItems().addAll(lineItems);
        return purchaseOrderRepository.save(purchaseOrder);
    }
}
