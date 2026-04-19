package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductDto;
import com.microservices.cartservice.exception.BusinessException;
import com.microservices.cartservice.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ProductClientImpl implements ProductClient{

    private static final Logger logger = LoggerFactory.getLogger(ProductClientImpl.class);

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

        } catch (WebClientResponseException.NotFound ex) {
            logger.error("Product not found for id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);

        } catch (Exception ex) {
            logger.error("Error calling Product Service", ex);
            throw new BusinessException("Failed to fetch product details");
        }
    }
}
