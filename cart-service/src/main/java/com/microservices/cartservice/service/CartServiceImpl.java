package com.microservices.cartservice.service;

import com.microservices.cartservice.entity.Cart;
import com.microservices.cartservice.exception.BusinessException;
import com.microservices.cartservice.exception.ResourceNotFoundException;
import com.microservices.cartservice.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    //Create Cart
    @Override
    public Cart createCart(Cart cart) {
        logger.info("Creating new cart");
        try {
            validateCart(cart);
            Cart savedCart = cartRepository.save(cart);
            logger.info("Cart created successfully with id: {}", savedCart.getId());
            return savedCart;
        } catch (BusinessException ex) {
            logger.error("Validation failed while creating cart: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while creating cart", ex);
            throw new BusinessException("Failed to create cart");
        }
    }

    @Override
    public Cart getCartById(Integer id) {
        logger.info("Fetching cart by id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart id: {}", id);
            throw new BusinessException("Cart id must be greater than 0");
        }

        return cartRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cart not found with id: {}", id);
                    return new ResourceNotFoundException("Cart not found with id: " + id);
                });
    }

    @Override
    public List<Cart> getAllCarts() {
        logger.info("Fetching all carts");
        List<Cart> carts = cartRepository.findAll();
        logger.info("Fetched {} carts", carts.size());
        return carts;
    }

    @Override
    public Cart updateCart(Integer id, Cart cart) {
        logger.info("Updating cart with id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart id for update: {}", id);
            throw new BusinessException("Cart id must be greater than 0");
        }

        try {
            Cart existingCart = getCartById(id);
            validateCart(cart);

            existingCart.setUserId(cart.getUserId());

            Cart updatedCart = cartRepository.save(existingCart);
            logger.info("Cart updated successfully with id: {}", updatedCart.getId());

            return updatedCart;
        } catch (ResourceNotFoundException | BusinessException ex) {
            logger.error("Error while updating cart id {}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while updating cart", ex);
            throw new BusinessException("Failed to update cart with id: " + id);
        }
    }

    @Override
    public void deleteCart(Integer id) {
        logger.info("Deleting cart with id: {}", id);

        if (id == null || id <= 0) {
            logger.error("Invalid cart id for deletion: {}", id);
            throw new BusinessException("Cart id must be greater than 0");
        }

        Cart existingCart = getCartById(id);
        cartRepository.delete(existingCart);

        logger.info("Cart deleted successfully with id: {}", id);
    }

    @Override
    public boolean existsCart(Integer cartId) {

        logger.info("Checking existence of cart with id: {}", cartId);

        if (cartId == null || cartId <= 0) {
            logger.error("Invalid cart id for existence check: {}", cartId);
            throw new BusinessException("Cart id must be greater than 0");
        }

        boolean exists = cartRepository.existsById(cartId);
        logger.info("Cart existence for id {}: {}", cartId, exists);

        return exists;
    }

    @Override
    public List<Cart> getCartsByUserId(Integer userId) {
        logger.info("Fetching carts by userId: {}", userId);

        if (userId == null || userId <= 0) {
            logger.error("Invalid userId: {}", userId);
            throw new BusinessException("User id must be greater than 0");
        }

        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts.isEmpty()) {
            logger.warn("No carts found for userId: {}", userId);
            throw new ResourceNotFoundException("No carts found for userId: " + userId);
        }

        logger.info("Fetched {} carts for userId: {}", carts.size(), userId);
        return carts;
    }




    private void validateCart(Cart cart) {
        if (cart == null) {
            throw new BusinessException("Cart cannot be null");
        }

        if (cart.getUserId() == null || cart.getUserId() <= 0) {
            throw new BusinessException("UserId must be greater than 0");
        }
    }
}
