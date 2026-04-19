package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductDto;
import com.microservices.cartservice.entity.CartEvent;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.exception.BusinessException;
import com.microservices.cartservice.exception.ResourceNotFoundException;
import com.microservices.cartservice.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final CartItemRepository cartItemRepository;

    private final CartAsyncValidationService cartAsyncValidationService;

    private final CartEventProducer cartEventProducer;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               CartEventProducer cartEventProducer, CartAsyncValidationService cartAsyncValidationService) {
        this.cartItemRepository = cartItemRepository;
        this.cartAsyncValidationService = cartAsyncValidationService;
        this.cartEventProducer = cartEventProducer;
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {

        logger.info("Request received to add cart item");

        try {
            ProductDto product = cartAsyncValidationService.validateCartItemAsync(cartItem);
            logger.info("Cart item validation successful for productId: {}", product.getId());

            CartItem savedItem = cartItemRepository.save(cartItem);
            logger.info("Cart item added successfully with id: {}", savedItem.getId());

            publishCartEvent(savedItem);

            return savedItem;
        } catch (BusinessException ex) {
            logger.error("Business exception while adding cart item: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while adding cart item", ex);
            throw new BusinessException("Failed to add cart item");
        }
    }

    @Override
    public CartItem getCartItemById(Integer id) {
        logger.info("Request received to fetch cart item by id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart item id: {}", id);
            throw new BusinessException("Cart item id must be greater than 0");
        }

        return cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cart item not found with id: {}", id);
                    return new ResourceNotFoundException("Cart item not found with id: " + id);
                });
    }

    @Override
    public List<CartItem> getAllCartItems() {
        logger.info("Request received to fetch all cart items");
        List<CartItem> cartItems = cartItemRepository.findAll();
        logger.info("Fetched {} cart items", cartItems.size());
        return cartItems;
    }

    @Override
    public CartItem updateCartItem(Integer id, CartItem cartItem) {

        logger.info("Request received to update cart item with id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart item id for update: {}", id);
            throw new BusinessException("Cart item id must be greater than 0");
        }

        try {
            CartItem existingItem = getCartItemById(id);

            ProductDto product = cartAsyncValidationService.validateCartItemAsync(cartItem);
            logger.info("Cart item validation successful for update, productId: {}", product.getId());

            existingItem.setCartId(cartItem.getCartId());
            existingItem.setProductId(cartItem.getProductId());
            existingItem.setQuantity(cartItem.getQuantity());

            CartItem updatedItem = cartItemRepository.save(existingItem);
            logger.info("Cart item updated successfully with id: {}", updatedItem.getId());

            publishCartEvent(updatedItem);

            return updatedItem;
        } catch (ResourceNotFoundException | BusinessException ex) {
            logger.error("Error while updating cart item with id {} : {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while updating cart item with id: {}", id, ex);
            throw new BusinessException("Failed to update cart item with id: " + id);
        }
    }

    @Override
    public void deleteCartItem(Integer id) {
        logger.info("Request received to delete cart item with id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart item id for deletion: {}", id);
            throw new BusinessException("Cart item id must be greater than 0");
        }

        CartItem existingItem = getCartItemById(id);
        cartItemRepository.delete(existingItem);

        logger.info("Cart item deleted successfully with id: {}", id);
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Integer cartId) {
        logger.info("Request received to fetch cart items by cartId: {}", cartId);

        if (cartId == null || cartId <= 0) {
            logger.error("Invalid cartId: {}", cartId);
            throw new BusinessException("Cart id must be greater than 0");
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        if (cartItems.isEmpty()) {
            logger.warn("No cart items found for cartId: {}", cartId);
            throw new ResourceNotFoundException("No cart items found for cartId: " + cartId);
        }

        logger.info("Fetched {} cart items for cartId: {}", cartItems.size(), cartId);
        return cartItems;
    }

    @Override
    public List<CartItem> getCartItemsByProductId(Integer productId) {
        logger.info("Request received to fetch cart items by productId: {}", productId);

        if (productId == null || productId <= 0) {
            logger.error("Invalid productId: {}", productId);
            throw new BusinessException("Product id must be greater than 0");
        }

        List<CartItem> cartItems = cartItemRepository.findByProductId(productId);

        if (cartItems.isEmpty()) {
            logger.warn("No cart items found for productId: {}", productId);
            throw new ResourceNotFoundException("No cart items found for productId: " + productId);
        }

        logger.info("Fetched {} cart items for productId: {}", cartItems.size(), productId);
        return cartItems;
    }

    @Override
    public void deleteCartItemsByCartId(Integer cartId) {
        logger.info("Request received to delete cart items by cartId: {}", cartId);

        if (cartId == null || cartId <= 0) {
            logger.error("Invalid cartId for deletion: {}", cartId);
            throw new BusinessException("Cart id must be greater than 0");
        }

        cartItemRepository.deleteByCartId(cartId);
        logger.info("Delete operation completed for cartId: {}", cartId);
    }

    @Override
    public boolean existsCartItem(Integer cartItemId) {

        logger.info("Checking existence of cart item with id: {}", cartItemId);

        if (cartItemId == null || cartItemId <= 0) {
            logger.error("Invalid cart item id for existence check: {}", cartItemId);
            throw new BusinessException("Cart item id must be greater than 0");
        }

        boolean exists = cartItemRepository.existsById(cartItemId);
        logger.info("Cart item exists status for id {} : {}", cartItemId, exists);

        return exists;
    }

    private void publishCartEvent(CartItem cartItem) {
        try {
            CartEvent event = new CartEvent(
                    cartItem.getCartId(),
                    cartItem.getProductId(),
                    cartItem.getQuantity()
            );

            cartEventProducer.publishCartEvent(event);
            logger.info("Cart event published successfully for cart item id: {}", cartItem.getId());
        } catch (Exception ex) {
            logger.error("Failed to publish cart event for cart item id: {}", cartItem.getId(), ex);
            throw new BusinessException("Cart item saved, but failed to publish cart event");
        }
    }

}
