package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductDto;

public interface ProductClient {

    ProductDto getProductById(Integer productId);
}
