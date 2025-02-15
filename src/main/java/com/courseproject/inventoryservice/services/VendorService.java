package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VendorService {
    @Autowired
    VendorRepository vendorRepository;

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.saveAndFlush(vendor);
    }

    public Vendor getVendorById(UUID uuid) {
        return vendorRepository.findById(uuid).orElse(null);
    }
}
