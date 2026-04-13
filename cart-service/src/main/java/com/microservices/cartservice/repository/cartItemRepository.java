package com.microservices.cartservice.repository;

import com.microservices.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface cartItemRepository extends JpaRepository <CartItem, Integer> {
}
