package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Vendor;
import com.courseproject.inventoryservice.repositories.VendorRepository;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final RedisTemplate<Long, Vendor> redisTemplate;
    private Tracer tracer;

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Observed(name = "get.vendor",
            contextualName = "getting-vendor",
            lowCardinalityKeyValues = {"vendor"}
    )
    public Vendor getVendorById(Long id) throws EntityNotFoundException {
        ScopedSpan span = tracer.startScopedSpan("getVendorById");
        try {
            Vendor v = redisTemplate.opsForValue().get(id);
            if (v == null) {
                log.info("Vendor with id {} not found in Redis", id);
                v = vendorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
                redisTemplate.opsForValue().set(id, v);
                log.info("Vendor with id {} was added to Redis", id);
            } else log.info("Vendor with id {} was found in Redis", id);
            return v;
        } catch (Exception e) {
            throw new EntityNotFoundException(String.format("Vendor with id %s not found", id), e);
        } finally {
            span.tag("vendor.service", "Redis");
            span.event("getVendorById");
            span.end();
        }
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
