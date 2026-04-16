package com.microservices.cartservice.repository;

import com.microservices.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository <CartItem, Integer> {

    List<CartItem> findByCartId(Integer cartId);

    List<CartItem> findByProductId(Integer productId);

    void deleteByCartId(Integer cartId);
}
