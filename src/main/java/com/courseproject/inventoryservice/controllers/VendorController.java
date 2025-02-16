package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/vendor/")
public class VendorController {

    @Autowired
    VendorService vendorService;

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getVendor(@PathVariable("id") UUID uuid) {
        try {
            Vendor vendor = vendorService.getVendorById(uuid);
            return vendor != null ? ResponseEntity.ok(vendor) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody Vendor vendor) {
        try {
            Vendor savedVendor = vendorService.createVendor(vendor);
            return savedVendor != null ? ResponseEntity.ok(savedVendor) : ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
