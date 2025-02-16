package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/order/")
public class PurchaseOrderController {

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @GetMapping("{id}")
    public ResponseEntity<?> getPurchaseOrder(@PathVariable("id") UUID uuid) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(uuid);
            return purchaseOrder != null ? ResponseEntity.ok(purchaseOrder) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
