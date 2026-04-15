package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.CartItem;

import java.util.List;

public interface CartItemService {

    CartItem addCartItem(CartItem cartItem);

    CartItem getCartItemById(Integer id);

    List<CartItem> getAllCartItems();

    CartItem updateCartItem(Integer id, CartItem cartItem);

    void deleteCartItem(Integer id);

    List<CartItem> getCartItemsByCartId(Integer cartId);

    List<CartItem> getCartItemsByProductId(Integer productId);

    void deleteCartItemsByCartId(Integer cartId);

    boolean existsCartItem(Integer cartItemId);
}
