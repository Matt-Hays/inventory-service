package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.repositories.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor getVendorById(Long id) throws EntityNotFoundException {
        return vendorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Vendor updateVendor(Long id, Vendor vendor) throws EntityNotFoundException {
        Vendor oldVendor = getVendorById(id);
        if (vendor.getName() != null) oldVendor.setName(vendor.getName());
        if (vendor.getAddress() != null) oldVendor.setAddress(vendor.getAddress());
        if (vendor.getEmail() != null) oldVendor.setEmail(vendor.getEmail());
        if (vendor.getPhone() != null) oldVendor.setPhone(vendor.getPhone());
        return vendorRepository.save(oldVendor);
    }

    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
}
