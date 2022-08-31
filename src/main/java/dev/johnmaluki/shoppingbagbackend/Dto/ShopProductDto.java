package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopProductDto {
    private long id;
    private Product product;
    private BigDecimal price;
}
