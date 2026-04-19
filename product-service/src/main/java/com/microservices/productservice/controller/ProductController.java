package com.microservices.productservice.controller;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        logger.info("Received request to create product");
        Product savedProduct = productService.createProduct(product);
        logger.info("Product created successfully with id: {}", savedProduct.getId());
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        logger.info("Received request to fetch product by id: {}", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products");
        List<Product> products = productService.getAllProducts();
        logger.info("Fetched {} products", products.size());
        return ResponseEntity.ok(products);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                 @Valid @RequestBody Product product) {
        logger.info("Received request to update product with id: {}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        logger.info("Product updated successfully with id: {}", updatedProduct.getId());
        return ResponseEntity.ok(updatedProduct);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        logger.info("Received request to delete product with id: {}", id);
        productService.deleteProduct(id);
        logger.info("Product deleted successfully with id: {}", id);
        return ResponseEntity.ok("Product deleted successfully with id: " + id);
    }
    @GetMapping("/{productId}/validate-stock/{quantity}")
    public ResponseEntity<Boolean> validateStock(@PathVariable Integer productId,
                                                 @PathVariable Integer quantity) {
        logger.info("Received request to validate stock for productId: {} and quantity: {}", productId, quantity);
        boolean isValid = productService.validateStock(productId, quantity);
        return ResponseEntity.ok(isValid);
    }
    @GetMapping("/exists/{productId}")
    public ResponseEntity<Boolean> existsProduct(@PathVariable Integer productId) {
        logger.info("Received request to check existence of productId: {}", productId);
        boolean exists = productService.existsProduct(productId);
        return ResponseEntity.ok(exists);
    }
    // Pagination + Sorting
    @GetMapping("/paged")
    public Page<Product> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        logger.info("Received request to fetch paged products: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        return productService.getAllProductsPaged(page, size, sortBy, sortDir);
    }

    // Java Streams - Filtering
    @GetMapping("/filter/price-greater-than/{price}")
    public List<Product> getProductsByPriceGreaterThan(@PathVariable Double price) {
        logger.info("Received request to fetch products with price greater than: {}", price);
        return productService.getProductsByPriceGreaterThan(price);
    }

    // Java Streams - Transforming
    @GetMapping("/names")
    public List<String> getAllProductNames() {
        logger.info("Received request to fetch all product names");
        return productService.getAllProductNames();
    }

    @GetMapping("/native/price-greater-than/{price}")
    public List<Product> getProductsAbovePriceNative(@PathVariable Double price) {
        logger.info("Received request to fetch products above price using native query: {}", price);
        return productService.findProductsWithPriceGreaterThan(price);
    }

}
