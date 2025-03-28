package com.mbc.day05.domain;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Long productId;  // AUTO_INCREMENT
    private String name;
    private String description;
    @NumberFormat(pattern = "#,###")
    private int price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

