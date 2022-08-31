package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBagProduct;

import java.util.List;

public interface ShoppingBagProductService {
    List<ShoppingBagProduct> findAllShoppingBagProducts(long shoppingBagId);
    ShoppingBagProduct updateShoppingBagProductQuantity(long shoppingBagProductId, int quantity);
}
