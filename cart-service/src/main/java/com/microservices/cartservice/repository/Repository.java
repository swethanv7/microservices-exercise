package com.microservices.cartservice.repository;

import com.microservices.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Cart, Integer> {
}
