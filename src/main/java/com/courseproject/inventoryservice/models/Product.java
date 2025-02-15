package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
public class Product {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Double price;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "id")
    private Inventory inventory;

//    @Min(0)
//    private long quantity;

    @ManyToMany
    @JoinTable(
            name = "product_purchase_order",
            joinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "ID")
    )
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank String description) {
        this.description = description;
    }

    public @NotNull @Min(0) Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull @Min(0) Double price) {
        this.price = price;
    }

//    @Min(0)
//    public long getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(@Min(0) long quantity) {
//        this.quantity = quantity;
//    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
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
        sb.append("Product{")
                .append("ID=").append(id)
                .append(", Version=").append(version)
                .append(", Name='").append(name).append("'")
                .append(", PurchaseOrders={");

        for (PurchaseOrder po : purchaseOrders) {
            sb.append(po.getId()).append(",");
        }

        if (!purchaseOrders.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}}");

        return sb.toString();
    }
}
