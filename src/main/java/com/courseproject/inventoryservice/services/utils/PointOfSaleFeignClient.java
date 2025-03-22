package com.courseproject.inventoryservice.services.utils;

import com.courseproject.inventoryservice.configuration.FeignClientConfiguration;
import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.dto.PointOfSaleProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "point-of-sale-service",
        configuration = FeignClientConfiguration.class
)
public interface PointOfSaleFeignClient {
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/v1/product",
            consumes = "application/json"
    )
    ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String token, @RequestBody PointOfSaleProductDTO product);
}
