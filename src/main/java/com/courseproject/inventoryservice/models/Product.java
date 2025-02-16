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

    @NotNull
    @Min(0)
    private Double quantity;

    @OneToMany
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();
}
