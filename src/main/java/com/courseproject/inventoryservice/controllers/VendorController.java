package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.services.VendorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("v1/vendor")
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(vendorService.getVendorById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public Vendor createVendor(@RequestBody @Valid Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable UUID id, @RequestBody @Valid Vendor vendor) {
        try {
            return ResponseEntity.ok(vendorService.updateVendor(id, vendor));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable UUID id) {
        vendorService.deleteVendor(id);
    }
}
