package com.courseproject.inventoryservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "id")
    private Inventory inventory;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{")
                .append("ID=").append(id)
                .append(", Version=").append(version)
                .append(", Name='").append(name).append("'")
                .append("}");

        return sb.toString();
    }
}
