package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.CartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(CartEventProducer.class);

    private static final String TOPIC_NAME = "cart-events";

    private final KafkaTemplate<String, CartEvent> kafkaTemplate;

    public CartEventProducer(KafkaTemplate<String, CartEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartEvent(CartEvent event) {

        logger.info("Publishing Kafka event to topic {}: {}", TOPIC_NAME, event);
        kafkaTemplate.send(TOPIC_NAME, event);
    }
}
