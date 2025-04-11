package com.courseproject.inventoryservice.configuration;

import com.courseproject.inventoryservice.models.Vendor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<Long, Vendor> vendorRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Vendor> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
