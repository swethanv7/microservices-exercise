package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.CartEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {
    private static final String TOPIC_NAME = "cart-events";

    private final KafkaTemplate<String, CartEvent> kafkaTemplate;

    public CartEventProducer(KafkaTemplate<String, CartEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCartEvent(CartEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event);
    }
}
