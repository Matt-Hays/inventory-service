package com.courseproject.inventoryservice.services;

import com.courseproject.inventoryservice.models.Inventory;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.repositories.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InventoryService {
    InventoryRepository inventoryRepository;
    JdbcTemplate jdbcTemplate;

    public Inventory getInventory(UUID uuid) {
        return inventoryRepository.findById(uuid).orElse(null);
    }

    public Inventory sellProduct(UUID id, long qty) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory == null) {
            return null;
        }

        if (inventory.getQuantity() < qty) {
            throw new Exception("not enough stock: " + inventory.getQuantity() + "/" + qty);
        }

        Long newQty = inventory.getQuantity() - qty;

        try {
            jdbcTemplate.execute("BEGIN;");
            jdbcTemplate.update("UPDATE inventory SET quantity = ? WHERE id = ?", newQty, id);
            jdbcTemplate.execute("COMMIT;");
            return inventoryRepository.findById(id).orElse(null);
        } catch (Exception e) {
            jdbcTemplate.execute("ROLLBACK;");
            throw e;
        }
    }

    public void arriveLineItems(Set<PurchaseOrderLineItem> purchaseOrderLineItems) {

        for (PurchaseOrderLineItem purchaseOrderLineItem : purchaseOrderLineItems) {
            Inventory inventory = inventoryRepository.findById(purchaseOrderLineItem.getProduct().getId()).orElse(null);

            if (null == inventory) {
                inventory = new Inventory();
                inventory.setId(purchaseOrderLineItem.getProduct().getId());
                inventory.setProduct(purchaseOrderLineItem.getProduct());
                inventory.setQuantity(purchaseOrderLineItem.getQuantity());
            } else {
                inventory.setQuantity(inventory.getQuantity() + purchaseOrderLineItem.getQuantity());
            }

            inventoryRepository.saveAndFlush(inventory);
        }
    }

}
