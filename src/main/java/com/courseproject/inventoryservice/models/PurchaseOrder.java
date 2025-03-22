package com.courseproject.inventoryservice.models;

import com.courseproject.inventoryservice.models.enums.PurchaseOrderStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "purchaseOrderLineItems")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @CreationTimestamp
    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PurchaseOrderStatus status = PurchaseOrderStatus.CREATED;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Vendor vendor;

    @OneToMany(mappedBy ="purchaseOrder", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ToString.Exclude
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();
}
