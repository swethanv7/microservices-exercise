package com.microservices.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Product name must not be empty")
    private String name;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock must not be null")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
}
