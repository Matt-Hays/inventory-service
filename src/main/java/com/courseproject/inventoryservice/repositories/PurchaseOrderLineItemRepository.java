package com.courseproject.inventoryservice.repositories;

import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseOrderLineItemRepository extends JpaRepository<PurchaseOrderLineItem, UUID> {
}
