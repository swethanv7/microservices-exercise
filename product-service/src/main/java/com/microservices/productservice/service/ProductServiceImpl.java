package com.microservices.productservice.service;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    //Constructor Injection
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // Create Product Logic
    @Override
    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    // Get product by Product ID
    @Override
    public Product getProductById(Integer id) {
       return productRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Product not found with id: " + id));
    }

    //Get all the products
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //Update the existing product details
    @Override
    public Product updateProduct(Integer id, Product product) {
        Product existingProduct = getProductById(id);

        validateProduct(product); // To validate the product that we are getting from the user (similar to backend validation)

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());

        return productRepository.save(existingProduct);
    }

    // Delete the product by its ID
    @Override
    public void deleteProduct(Integer id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    // To check the stock of the product
    @Override
    public boolean validateStock(Integer productId, Integer quantity) {
        Product product = getProductById(productId);

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product id: " + productId);
        }

        return true;
    }

    @Override
    public boolean existsProduct(Integer productId) {
        return productRepository.existsById(productId);
    }

    // Method to Validate the Product
    private void validateProduct(Product product) {
        if (product == null) {
            throw new RuntimeException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Product name cannot be null or empty");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new RuntimeException("Product price must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
    }
}
