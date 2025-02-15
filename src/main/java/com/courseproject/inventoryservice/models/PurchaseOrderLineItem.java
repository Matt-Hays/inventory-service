package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PurchaseOrderLineItem {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    private @Min(1) long quantity;

    @ManyToOne()
    @JoinColumn(name = "PURCHASE_ORDER_ID", nullable = false)
    private PurchaseOrder purchaseOrder;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public @Min(1) long getQuantity() {
        return quantity;
    }

    public void setQuantity(@Min(1) long quantity) {
        this.quantity = quantity;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PurchaseOrderLineItem{" +
                "id=" + id +
                ", version=" + version +
                ", product=" + product +
                ", quantity=" + quantity +
                ", purchaseOrder=" + purchaseOrder +
                '}';
    }
}
