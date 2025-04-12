package com.courseproject.inventoryservice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "purchaseOrders")
@ToString
public class Vendor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @OneToMany(mappedBy = "vendor")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ToString.Exclude
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();
}
