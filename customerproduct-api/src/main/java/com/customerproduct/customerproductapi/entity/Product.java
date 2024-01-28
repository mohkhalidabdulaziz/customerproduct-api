package com.customerproduct.customerproductapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "book_price", nullable = false)
    private double bookPrice;

    @Column(name = "book_quantity")
    private int bookQuantity;
}

