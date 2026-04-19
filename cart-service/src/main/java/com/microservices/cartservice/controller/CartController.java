package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.service.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);


    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    // Create Cart
    @PostMapping
    public Cart createCart(@Valid @RequestBody Cart cart) {
        logger.info("Received request to create cart");
        Cart createdCart = cartService.createCart(cart);
        logger.info("Cart created successfully with id: {}", createdCart.getId());
        return createdCart;
    }

    // Get Cart by ID
    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Integer id) {
        logger.info("Received request to get cart by id: {}", id);
        return cartService.getCartById(id);
    }

    // Get All Carts
    @GetMapping
    public List<Cart> getAllCarts() {
        logger.info("Received request to get all carts");
        List<Cart> carts = cartService.getAllCarts();
        logger.info("Fetched {} carts", carts.size());
        return carts;
    }

    // Update Cart
    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable Integer id, @Valid @RequestBody Cart cart) {
        logger.info("Received request to update cart with id: {}", id);
        Cart updatedCart = cartService.updateCart(id, cart);
        logger.info("Cart updated successfully with id: {}", updatedCart.getId());
        return updatedCart;
    }

    // Delete Cart
    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable Integer id) {
        logger.info("Received request to delete cart with id: {}", id);
        cartService.deleteCart(id);
        logger.info("Cart deleted successfully with id: {}", id);
        return "Cart deleted successfully with id: " + id;
    }

    // Check Cart Exists
    @GetMapping("/exists/{cartId}")
    public boolean existsCart(@PathVariable Integer cartId) {
        logger.info("Received request to check cart existence for id: {}", cartId);
        return cartService.existsCart(cartId);
    }

    // Get Cart(s) by UserId
    @GetMapping("/user/{userId}")
    public List<Cart> getCartsByUserId(@PathVariable Integer userId) {
        logger.info("Received request to get carts by userId: {}", userId);
        List<Cart> carts = cartService.getCartsByUserId(userId);
        logger.info("Fetched {} carts for userId: {}", carts.size(), userId);
        return carts;
    }


}
