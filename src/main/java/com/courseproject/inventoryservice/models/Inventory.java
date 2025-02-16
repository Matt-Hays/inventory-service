package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Inventory {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Version
    private Long version;

    @OneToOne(mappedBy = "inventory")
    private Product product;

    @Min(0)
    private long quantity;

}
