package com.microservices.cartservice.controller;

import com.microservices.cartservice.entity.CartItem;
import com.microservices.cartservice.service.CartItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private static final Logger logger = LoggerFactory.getLogger(CartItemController.class);

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }
    // Add Cart Item
    @PostMapping
    public CartItem addCartItem(@Valid @RequestBody CartItem cartItem) {
        logger.info("Received add cart item request: cartId={}, productId={}, quantity={}",
                cartItem.getCartId(), cartItem.getProductId(), cartItem.getQuantity());
        return cartItemService.addCartItem(cartItem);
    }

    // Get Cart Item by ID
    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable Integer id) {
        logger.info("Received get cart item request for id={}", id);
        return cartItemService.getCartItemById(id);
    }

    // Get All Cart Items
    @GetMapping
    public List<CartItem> getAllCartItems() {
        logger.info("Received request to fetch all cart items");
        return cartItemService.getAllCartItems();
    }

    // Update Cart Item
    @PutMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Integer id,
                                   @Valid @RequestBody CartItem cartItem) {
        logger.info("Received update cart item request for id={}", id);
        return cartItemService.updateCartItem(id, cartItem);
    }

    // Delete Cart Item by ID
    @DeleteMapping("/{id}")
    public String deleteCartItem(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
        logger.info("Received delete cart item request for id={}", id);
        return "Cart item deleted successfully with id: " + id;
    }

    // Get Cart Items by Cart ID
    @GetMapping("/cart/{cartId}")
    public List<CartItem> getCartItemsByCartId(@PathVariable Integer cartId) {
        logger.info("Received get cart item request for Cart Id={}", cartId);
        return cartItemService.getCartItemsByCartId(cartId);
    }

    // Get Cart Items by Product ID
    @GetMapping("/product/{productId}")
    public List<CartItem> getCartItemsByProductId(@PathVariable Integer productId) {
        logger.info("Received get cart item request for Product Id={}", productId);
        return cartItemService.getCartItemsByProductId(productId);
    }

    // Delete Cart Items by Cart ID (⚠️ see note below)
    @DeleteMapping("/cart/{cartId}")
    public String deleteCartItemsByCartId(@PathVariable Integer cartId) {
        cartItemService.deleteCartItemsByCartId(cartId);
        logger.info("Received delete cart item request for Cart Id={}", cartId);
        return "Cart items deleted for cartId: " + cartId;
    }

    // Check if Cart Item Exists
    @GetMapping("/exists/{cartItemId}")
    public boolean existsCartItem(@PathVariable Integer cartItemId) {
        logger.info("Received request to check cart item exists for id={}",cartItemId);
        return cartItemService.existsCartItem(cartItemId);
    }
}
