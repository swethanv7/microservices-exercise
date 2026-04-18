package com.microservices.cartservice.service;


import com.microservices.cartservice.dto.ProductDto;
import com.microservices.cartservice.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class CartAsyncValidationService {

    private final ProductClient productClient;

    private final Executor taskExecutor;

    public CartAsyncValidationService(ProductClient productClient, Executor taskExecutor){
        this.productClient = productClient;
        this.taskExecutor = taskExecutor;
    }

    // Validation service - > validating product, validating stock and fetching product from the DB in parllel
    public ProductDto validateCartItemAsync(CartItem cartItem) {

        CompletableFuture<Void> cartValidationFuture =
                CompletableFuture.runAsync(() -> {
                    System.out.println("Cart validation thread: " + Thread.currentThread().getName());
                    validateCartItem(cartItem);
                }, taskExecutor);

        CompletableFuture<ProductDto> productFuture =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("Product fetch thread: " + Thread.currentThread().getName());
                    ProductDto product = productClient.getProductById(cartItem.getProductId());
                    if (product == null) {
                        throw new RuntimeException("Product does not exist with id: " + cartItem.getProductId());
                    }
                    return product;
                }, taskExecutor);

        CompletableFuture<ProductDto> stockValidationFuture =
                cartValidationFuture.thenCombine(productFuture, (unused, product) -> {
                    if (product.getStock() == null || product.getStock() < cartItem.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for product id: " + cartItem.getProductId());
                    }
                    return product;
                });

        return stockValidationFuture.join();
    }

    private void validateCartItem(CartItem cartItem) {
        if (cartItem.getCartId() == null) {
            throw new RuntimeException("Cart ID must not be null");
        }
        if (cartItem.getProductId() == null) {
            throw new RuntimeException("Product ID must not be null");
        }
        if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
    }


}
