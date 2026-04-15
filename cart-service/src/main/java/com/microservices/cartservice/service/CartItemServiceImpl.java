package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository){
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {
        validateCartItem(cartItem);
        return cartItemRepository.save(cartItem);
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

        validateCartItem(cartItem);

        existingItem.setCartId(cartItem.getCartId());
        existingItem.setProductId(cartItem.getProductId());
        existingItem.setQuantity(cartItem.getQuantity());

        return cartItemRepository.save(existingItem);
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
    public void deleteCartItemsByCartId(Integer id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public boolean existsCartItem(Integer cartItemId) {
        return cartItemRepository.existsById(cartItemId);
    }

    private void validateCartItem(CartItem cartItem) {
        if (cartItem == null) {
            throw new RuntimeException("CartItem cannot be null");
        }

        if (cartItem.getCartId() == null || cartItem.getCartId() <= 0) {
            throw new RuntimeException("Invalid cartId");
        }

        if (cartItem.getProductId() == null || cartItem.getProductId() <= 0) {
            throw new RuntimeException("Invalid productId");
        }

        if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
    }
}
