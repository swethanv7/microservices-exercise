package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.service.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }
    // Add Cart Item
    @PostMapping
    public CartItem addCartItem(@RequestBody CartItem cartItem) {
        return cartItemService.addCartItem(cartItem);
    }

    // Get Cart Item by ID
    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable Integer id) {
        return cartItemService.getCartItemById(id);
    }

    // Get All Cart Items
    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    // Update Cart Item
    @PutMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Integer id,
                                   @RequestBody CartItem cartItem) {
        return cartItemService.updateCartItem(id, cartItem);
    }

    // Delete Cart Item by ID
    @DeleteMapping("/{id}")
    public String deleteCartItem(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
        return "Cart item deleted successfully with id: " + id;
    }

    // Get Cart Items by Cart ID
    @GetMapping("/cart/{cartId}")
    public List<CartItem> getCartItemsByCartId(@PathVariable Integer cartId) {
        return cartItemService.getCartItemsByCartId(cartId);
    }

    // Get Cart Items by Product ID
    @GetMapping("/product/{productId}")
    public List<CartItem> getCartItemsByProductId(@PathVariable Integer productId) {
        return cartItemService.getCartItemsByProductId(productId);
    }

    // Delete Cart Items by Cart ID (⚠️ see note below)
    @DeleteMapping("/cart/{cartId}")
    public String deleteCartItemsByCartId(@PathVariable Integer cartId) {
        cartItemService.deleteCartItemsByCartId(cartId);
        return "Cart items deleted for cartId: " + cartId;
    }

    // Check if Cart Item Exists
    @GetMapping("/exists/{cartItemId}")
    public boolean existsCartItem(@PathVariable Integer cartItemId) {
        return cartItemService.existsCartItem(cartItemId);
    }
}
