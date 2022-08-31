package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;

import java.util.List;

public interface ShopProductService {
    List<ShopProduct> getShopProducts(long shopId);
}
