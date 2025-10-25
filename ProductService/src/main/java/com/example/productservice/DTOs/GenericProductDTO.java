package com.example.productservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericProductDTO {
    private String id;
    private String title;
    private Double price;
    private String category;
    private String description;
    private String image;
}
