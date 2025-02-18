package com.courseproject.inventoryservice.models;

import com.courseproject.inventoryservice.models.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "purchaseOrderLineItems")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PurchaseOrderStatus status = PurchaseOrderStatus.CREATED;

    @ManyToOne
    private Vendor vendor;

    @OneToMany(mappedBy ="purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();
}
