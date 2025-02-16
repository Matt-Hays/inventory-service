package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.services.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("v1/inventory")
public class InventoryController {
//    @Value("${test.value}")
//    String testValue;

    private InventoryService inventoryService;

//    @GetMapping()
//    public String sayHello() {
//        return "Test Value: " + testValue;
//    }

    // Allocate inventory for a sale
//    @PostMapping("/sell/{id}/{qty}")
//    public ResponseEntity<?> sellProduct(@PathVariable UUID id, @PathVariable Double qty) {
//        try {
//            Product product = inventoryService.sellProduct(id, qty);
//            return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
