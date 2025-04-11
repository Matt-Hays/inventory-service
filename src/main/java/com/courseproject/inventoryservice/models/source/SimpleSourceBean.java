package com.courseproject.inventoryservice.models.source;

import com.courseproject.inventoryservice.models.ProductChangeModel;
import com.courseproject.inventoryservice.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {

	private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

	@Autowired
	private StreamBridge streamBridge;

	public void publishProductChange(String action, Long productId) {
		logger.info("Sending Kafka message {} for Product Id: {}", action, productId);

		ProductChangeModel productChangeModel = new ProductChangeModel(
				ProductChangeModel.class.getTypeName(),
				action,
				productId,
				UserContext.getCorrelationId());

		streamBridge.send("productChangeModelProducer-out-0", productChangeModel);
	}

	@Bean
	public MessageConverter kafkaMessageConverter() {
		return new StringJsonMessageConverter();
	}
}
