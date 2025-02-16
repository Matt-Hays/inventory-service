package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.services.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("v1/inventory")
public class InventoryController {
    private InventoryService inventoryService;


    // Allocate inventory for a sale
    @PostMapping("/sell/{id}/{qty}")
    public ResponseEntity<?> sellProduct(@PathVariable UUID id, @PathVariable Double qty) {
        try {
            Product product = inventoryService.sellProduct(id, qty);
            return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/receive/{id}")
    public ResponseEntity<?> purchaseOrders(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(inventoryService.receivePurchaseOrder(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
