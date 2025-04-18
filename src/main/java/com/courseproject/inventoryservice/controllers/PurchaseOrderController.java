package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.services.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Add @ControllerAdvice for error handling.
@RestController
@AllArgsConstructor
@RequestMapping("v1/purchase")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.findAllPurchaseOrders();
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(purchaseOrderService.findPurchaseOrderById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody @Valid PurchaseOrder purchaseOrder) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrder);
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PatchMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable Long id, @RequestBody @Valid PurchaseOrder purchaseOrder) {
        try {
            return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(id, purchaseOrder));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PatchMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receivePurchaseOrder(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.receivePurchaseOrder(token, id));
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PostMapping("/{id}/lineItems")
    public ResponseEntity<?> addLineItem(@PathVariable Long id, @RequestBody @Valid List<PurchaseOrderLineItem> purchaseOrderLineItems) {
        return ResponseEntity.ok(purchaseOrderService.addLineItemToPurchaseOrder(id, purchaseOrderLineItems));
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
    }
}
