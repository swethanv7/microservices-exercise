package com.microservices.productservice.service;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.exception.BusinessException;
import com.microservices.productservice.exception.ResourceNotFoundException;
import com.microservices.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    //Constructor Injection
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // Create Product Logic
    @Override
    public Product createProduct(Product product) {
        logger.info("Creating product");

        validateProduct(product);

        Product saved = productRepository.save(product);
        logger.info("Product created with id: {}", saved.getId());

        return saved;
    }

    // Get product by Product ID
    @Override
    public Product getProductById(Integer id) {
        logger.info("Fetching product by id: {}", id);

        if (id == null || id <= 0) {
            throw new BusinessException("Product id must be greater than 0");
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    //Get all the products
    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    //Update the existing product details
    @Override
    public Product updateProduct(Integer id, Product product) {
        logger.info("Updating product id: {}", id);

        if (id == null || id <= 0) {
            throw new BusinessException("Product id must be greater than 0");
        }

        Product existing = getProductById(id);
        validateProduct(product);

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());

        return productRepository.save(existing);
    }

    // Delete the product by its ID
    @Override
    public void deleteProduct(Integer id) {
        logger.info("Deleting product id: {}", id);

        if (id == null || id <= 0) {
            throw new BusinessException("Product id must be greater than 0");
        }

        Product existing = getProductById(id);
        productRepository.delete(existing);
    }

    // To check the stock of the product
    @Override
    public boolean validateStock(Integer productId, Integer quantity) {
        logger.info("Validating stock for productId: {}", productId);

        Product product = getProductById(productId);

        if (quantity == null || quantity <= 0) {
            throw new BusinessException("Quantity must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < quantity) {
            throw new BusinessException("Insufficient stock for product id: " + productId);
        }

        return true;
    }

    @Override
    public boolean existsProduct(Integer productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public Page<Product> getAllProductsPaged(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsByPriceGreaterThan(Double price) {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getPrice() != null && product.getPrice() > price)
                .toList();
    }

    @Override
    public List<String> getAllProductNames() {
        return productRepository.findAll()
                .stream()
                .map(Product::getName)
                .toList();
    }

    @Override
    public List<Product> findProductsWithPriceGreaterThan(Double price) {
        return productRepository.findProductsWithPriceGreaterThan(price);
    }

    // Method to Validate the Product
    private void validateProduct(Product product) {
        if (product == null) {
            throw new BusinessException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BusinessException("Product name cannot be null or empty");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new BusinessException("Product price must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new BusinessException("Product stock cannot be negative");
        }
    }
}
