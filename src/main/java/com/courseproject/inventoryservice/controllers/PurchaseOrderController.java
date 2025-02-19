package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.services.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Add @ControllerAdvice for error handling.
@RestController
@AllArgsConstructor
@RequestMapping("v1/purchase")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.findAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(purchaseOrderService.findPurchaseOrderById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody @Valid PurchaseOrder purchaseOrder) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrder);
    }

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

    @PatchMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receivePurchaseOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(purchaseOrderService.receivePurchaseOrder(id));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/lineItems")
    public ResponseEntity<?> addLineItem(@PathVariable Long id, @RequestBody @Valid List<PurchaseOrderLineItem> purchaseOrderLineItems) {
        try {
            return ResponseEntity.ok(purchaseOrderService.addLineItemToPurchaseOrder(id, purchaseOrderLineItems));
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
    }
}
