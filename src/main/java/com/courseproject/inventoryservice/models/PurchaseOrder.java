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
@ToString
public class PurchaseOrder {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "VENDOR_ID", nullable = false)
    private Vendor vendor;

    @OneToMany(mappedBy = "purchaseOrder")
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();

    @NotNull
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public @NotNull LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(@NotNull LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Set<PurchaseOrderLineItem> getPurchaseOrderLineItems() {
        return purchaseOrderLineItems;
    }

    public void setPurchaseOrderLineItems(Set<PurchaseOrderLineItem> purchaseOrderLineItems) {
        this.purchaseOrderLineItems = purchaseOrderLineItems;
    }

    public void addPurchaseOrderLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        purchaseOrderLineItems.add(purchaseOrderLineItem);
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
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
