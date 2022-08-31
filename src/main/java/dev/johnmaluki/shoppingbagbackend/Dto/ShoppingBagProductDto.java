package dev.johnmaluki.shoppingbagbackend.Dto;

import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import lombok.Data;

@Data
public class ShoppingBagProductDto {
    private long id;
    private ShopProduct shopProduct;
    private int quantity;
}
