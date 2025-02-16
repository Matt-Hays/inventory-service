package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @NotNull
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;

    @ManyToOne
    private Vendor vendor;

    @OneToMany(mappedBy ="purchaseOrder", cascade = CascadeType.ALL)
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();
}
