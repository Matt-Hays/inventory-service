package com.courseproject.inventoryservice.controllers;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.services.VendorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("v1/vendor")
public class VendorController {
    private final VendorService vendorService;

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vendorService.getVendorById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PostMapping
    public Vendor createVendor(@RequestBody @Valid Vendor vendor) {
        return vendorService.createVendor(vendor);
    }

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody @Valid Vendor vendor) {
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

    @PreAuthorize("hasAnyRole('INVENTORY_MANAGER', 'INVENTORY_WORKER')")
    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
    }
}
