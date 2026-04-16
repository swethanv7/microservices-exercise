package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ProductClientImpl implements ProductClient{

    private final WebClient webClient;

    public ProductClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        try {
            return webClient.get()
                    .uri("/products/{id}", productId)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            return null;
        }
    }
}
