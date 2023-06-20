package com.example.springreactivemongoDB.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@Builder
public class ProductDto {

    private String id;
    private String name;
    private int qty;
    private double price;
}
