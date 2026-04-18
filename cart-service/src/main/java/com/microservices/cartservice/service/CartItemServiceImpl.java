package com.microservices.cartservice.service;

import com.microservices.cartservice.dto.ProductDto;
import com.microservices.cartservice.entity.CartEvent;
import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{

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

        ProductDto product = cartAsyncValidationService.validateCartItemAsync(cartItem);

        CartItem savedItem = cartItemRepository.save(cartItem);

        CartEvent event = new CartEvent(
                savedItem.getCartId(),
                savedItem.getProductId(),
                savedItem.getQuantity()
        );

        cartEventProducer.publishCartEvent(event);

        return savedItem;
    }

    @Override
    public CartItem getCartItemById(Integer id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem updateCartItem(Integer id, CartItem cartItem) {

        CartItem existingItem = getCartItemById(id);

        ProductDto product = cartAsyncValidationService.validateCartItemAsync(cartItem);

        existingItem.setCartId(cartItem.getCartId());
        existingItem.setProductId(cartItem.getProductId());
        existingItem.setQuantity(cartItem.getQuantity());

        CartItem updatedItem = cartItemRepository.save(existingItem);

        CartEvent event = new CartEvent(
                updatedItem.getCartId(),
                updatedItem.getProductId(),
                updatedItem.getQuantity()
        );

        cartEventProducer.publishCartEvent(event);

        return updatedItem;
    }

    @Override
    public void deleteCartItem(Integer id) {
        CartItem existingItem = getCartItemById(id);
        cartItemRepository.delete(existingItem);
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Integer cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public List<CartItem> getCartItemsByProductId(Integer productId) {
        return cartItemRepository.findByProductId(productId);
    }

    @Override
    public void deleteCartItemsByCartId(Integer cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    public boolean existsCartItem(Integer cartItemId) {
        return cartItemRepository.existsById(cartItemId);
    }

}
