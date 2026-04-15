package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    // Create Cart
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    // Get Cart by ID
    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Integer id) {
        return cartService.getCartById(id);
    }

    // Get All Carts
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    // Update Cart
    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable Integer id, @RequestBody Cart cart) {
        return cartService.updateCart(id, cart);
    }

    // Delete Cart
    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable Integer id) {
        cartService.deleteCart(id);
        return "Cart deleted successfully with id: " + id;
    }

    // Check Cart Exists
    @GetMapping("/exists/{cartId}")
    public boolean existsCart(@PathVariable Integer cartId) {
        return cartService.existsCart(cartId);
    }

    // Get Cart(s) by UserId
    @GetMapping("/user/{userId}")
    public List<Cart> getCartsByUserId(@PathVariable Integer userId) {
        return cartService.getCartsByUserId(userId);
    }


}
