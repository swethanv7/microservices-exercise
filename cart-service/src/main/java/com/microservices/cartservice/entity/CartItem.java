package com.microservices.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = " cart-items")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "Card ID  must not be NULL")
    private Integer cartId;


    @NotNull(message = "Product ID must not be NULL")
    private Integer productId;

    @NotNull(message = "Quantity must not be NULL")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
}
