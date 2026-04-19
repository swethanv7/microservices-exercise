package com.microservices.cartservice.service;


import com.microservices.cartservice.dto.ProductDto;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

@Service
public class CartAsyncValidationService {

    private static final Logger logger = LoggerFactory.getLogger(CartAsyncValidationService.class);


    private final ProductClient productClient;

    private final Executor taskExecutor;

    public CartAsyncValidationService(ProductClient productClient, Executor taskExecutor){
        this.productClient = productClient;
        this.taskExecutor = taskExecutor;
    }

    // Validation service - > validating product, validating stock and fetching product from the DB in parllel
    public ProductDto validateCartItemAsync(CartItem cartItem) {
        try {
            CompletableFuture<Void> cartValidationFuture =
                    CompletableFuture.runAsync(() -> {
                        logger.info("Cart validation running in thread: {}", Thread.currentThread().getName());
                        validateCartItem(cartItem);
                    }, taskExecutor);

            CompletableFuture<ProductDto> productFuture =
                    CompletableFuture.supplyAsync(() -> {
                        logger.info("Product fetch running in thread: {}", Thread.currentThread().getName());

                        if (cartItem == null || cartItem.getProductId() == null) {
                            throw new BusinessException("Product ID must not be null");
                        }

                        ProductDto product = productClient.getProductById(cartItem.getProductId());

                        if (product == null) {
                            throw new BusinessException("Product does not exist with id: " + cartItem.getProductId());
                        }

                        return product;
                    }, taskExecutor);

            CompletableFuture<ProductDto> stockValidationFuture =
                    cartValidationFuture.thenCombine(productFuture, (unused, product) -> {
                        if (product.getStock() == null || product.getStock() < cartItem.getQuantity()) {
                            throw new BusinessException("Insufficient stock for product id: " + cartItem.getProductId());
                        }
                        return product;
                    });

            return stockValidationFuture.join();

        } catch (CompletionException ex) {
            if (ex.getCause() instanceof BusinessException businessException) {
                throw businessException;
            }
            logger.error("Unexpected error during async cart validation", ex);
            throw new BusinessException("Cart validation failed");
        } catch (Exception ex) {
            logger.error("Unexpected error during cart validation", ex);
            throw new BusinessException("Cart validation failed");
        }
    }

    private void validateCartItem(CartItem cartItem) {
        if (cartItem == null) {
            throw new BusinessException("Cart item must not be null");
        }
        if (cartItem.getCartId() == null) {
            throw new BusinessException("Cart ID must not be null");
        }
        if (cartItem.getProductId() == null) {
            throw new BusinessException("Product ID must not be null");
        }
        if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }
    }
}
