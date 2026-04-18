package com.microservices.productservice.service;

import com.microservices.productservice.event.CartEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CartEventConsumer {

    @KafkaListener(topics = "cart-events")
    public void consumeCartEvent(CartEvent event) {
        System.out.println("Received cart event from Kafka: " + event);
    }
}