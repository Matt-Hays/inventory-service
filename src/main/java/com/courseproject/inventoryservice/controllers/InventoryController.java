package com.courseproject.inventoryservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

	@Value("${test.value}")
	String testValue;

	@GetMapping()
	public String sayHello() {
		return "Test Value: " + testValue;
	}
}
