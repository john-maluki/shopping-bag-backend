package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.Brand;
import lombok.Data;

@Data
public class ProductDto {
    private long productId;
    private Brand brand;
    private String name;
    private String description;
    private String imageCode;
    private String imageUrl;
    private String sku;
    private String modelNumber;
}
