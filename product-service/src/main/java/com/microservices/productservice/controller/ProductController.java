package com.microservices.productservice.controller;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                 @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully with id: " + id);
    }
    @GetMapping("/{productId}/validate-stock/{quantity}")
    public ResponseEntity<Boolean> validateStock(@PathVariable Integer productId,
                                                 @PathVariable Integer quantity) {
        boolean isValid = productService.validateStock(productId, quantity);
        return ResponseEntity.ok(isValid);
    }
    @GetMapping("/exists/{productId}")
    public ResponseEntity<Boolean> existsProduct(@PathVariable Integer productId) {
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
        return productService.getAllProductsPaged(page, size, sortBy, sortDir);
    }

    // Java Streams - Filtering
    @GetMapping("/filter/price-greater-than/{price}")
    public List<Product> getProductsByPriceGreaterThan(@PathVariable Double price) {
        return productService.getProductsByPriceGreaterThan(price);
    }

    // Java Streams - Transforming
    @GetMapping("/names")
    public List<String> getAllProductNames() {
        return productService.getAllProductNames();
    }

    @GetMapping("/native/price-greater-than/{price}")
    public List<Product> getProductsAbovePriceNative(@PathVariable Double price) {
        return productService.findProductsWithPriceGreaterThan(price);
    }

}
