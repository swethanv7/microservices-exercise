package com.microservices.cartservice.repository;

import com.microservices.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);
}
