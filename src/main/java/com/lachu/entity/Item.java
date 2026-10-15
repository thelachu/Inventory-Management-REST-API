package com.lachu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "items")
@Setter
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Size(max = 100,message = "Name must not exceed 100 characters")
    @Column(nullable = false,length = 100)
    private String name;

    @Size(max = 500,message = "Description must not exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value =0,message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "Price must be positive")
    @Digits(integer = 10, fraction = 2,message = "Price at most have 2 decimal places")
    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal price;
}
