package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    //Create Cart
    @Override
    public Cart createCart(Cart cart) {
        validateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Integer id) {
        return cartRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Cart not found with id :" + id));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart updateCart(Integer id, Cart cart) {
        Cart existingcart = getCartById(id);
        validateCart(cart);
        existingcart.setUserId(cart.getUserId());
        return cartRepository.save(existingcart);
    }

    @Override
    public void deleteCart(Integer id) {
        Cart existingCart = getCartById(id);
        cartRepository.delete(existingCart);
    }

    @Override
    public boolean existsCart(Integer cartId) {
        return cartRepository.existsById(cartId);
    }

    @Override
    public List<Cart> getCartsByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }




    private void validateCart(Cart cart) {
        if (cart == null) {
            throw new RuntimeException("Cart cannot be null");
        }

        if (cart.getUserId() == null || cart.getUserId() <= 0) {
            throw new RuntimeException("UserId must be greater than 0");
        }
    }
}
