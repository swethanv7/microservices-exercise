package com.microservices.productservice.service;

import com.microservices.productservice.event.CartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CartEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CartEventConsumer.class);

    @KafkaListener(topics = "cart-events")
    public void consumeCartEvent(CartEvent event) {
        System.out.println("Received cart event from Kafka: " + event);
        logger.info("Received cart event from Kafka: {}", event);
    }
}