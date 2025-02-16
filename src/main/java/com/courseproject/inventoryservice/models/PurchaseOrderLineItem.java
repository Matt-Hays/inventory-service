package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PurchaseOrderLineItem {
    @Id
    @Getter
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @Setter
    @Getter
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Setter
    @Getter
    private @Min(1)
    long quantity;

    @Override
    public String toString() {
        return "PurchaseOrderLineItem{" +
                "id=" + id +
                ", version=" + version +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
