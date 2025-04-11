package com.courseproject.inventoryservice.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductChangeModel {
	private String type;
	private String action;
	private Long productId;
	private String correlationId;

	public ProductChangeModel(String type, String action, Long productId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.productId = productId;
		this.correlationId = correlationId;
	}

}
