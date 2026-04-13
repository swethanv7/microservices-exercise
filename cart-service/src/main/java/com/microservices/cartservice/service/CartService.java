package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);

    Cart getCartById(Integer id);

    List<Cart> getAllCarts();

    Cart updateCart(Integer id, Cart cart);

    void deleteCart(Integer id);

    boolean existsCart(Integer cartId);

    List<Cart> getCartsByUserId(Integer userId);
}
