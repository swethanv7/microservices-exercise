package com.microservices.productservice.service;

import com.microservices.productservice.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Integer id);

    List<Product> getAllProducts();

    Product updateProduct(Integer id, Product product);

    void deleteProduct(Integer id);

    boolean validateStock(Integer productId, Integer quantity);

    boolean existsProduct(Integer productId);

    Page<Product> getAllProductsPaged(int page, int size, String sortBy, String sortDir);

    List<Product> getProductsByPriceGreaterThan(Double price);

    List<String> getAllProductNames();
}
