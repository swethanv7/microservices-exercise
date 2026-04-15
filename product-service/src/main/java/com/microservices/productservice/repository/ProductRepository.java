package com.microservices.productservice.repository;

import com.microservices.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository <Product, Integer> {

    @Query(value = "SELECT * FROM products WHERE price > :price", nativeQuery = true)
    List<Product> findProductsWithPriceGreaterThan(Double price);
}
