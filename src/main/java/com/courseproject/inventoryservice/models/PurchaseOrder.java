package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PurchaseOrder {
    @Id
    @Getter
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VENDOR_ID", nullable = false)
    private Vendor vendor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();

    @NotNull
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;

    public void addPurchaseOrderLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        purchaseOrderLineItems.add(purchaseOrderLineItem);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("PurchaseOrder{")
                .append("ID=").append(id)
                .append(", Version=").append(version)
                .append(", Vendor=").append(vendor.getName())
                .append(", OrderDate=").append(orderDate)
                .append(", DeliveryDate=").append(deliveryDate)
                .append(", PurchaseOrderLineItems={");

        for (PurchaseOrderLineItem purchaseOrderLineItem : purchaseOrderLineItems) {
            sb.append(purchaseOrderLineItem.getId()).append(",");
        }

        if (!purchaseOrderLineItems.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("}}");

        return sb.toString();
    }
}
