package com.microservices.cartservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = " cart-items")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;
}
