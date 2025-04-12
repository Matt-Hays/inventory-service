package com.courseproject.inventoryservice.configuration;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.Vendor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${redis.port}")
    private String redisPort;

    @Value("${redis.server}")
    private String redisServer;

    @Bean
    RedisTemplate<Long, Vendor> vendorRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Vendor> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    RedisTemplate<Long, Product> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisServer, Integer.parseInt(redisPort)));
    }

}
