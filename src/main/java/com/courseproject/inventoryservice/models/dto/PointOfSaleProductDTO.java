package com.courseproject.inventoryservice.models.dto;

public record PointOfSaleProductDTO(
        Long id,
        String sku,
        String description,
        Double price
) {
}
