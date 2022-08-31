package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> getShops();
    List<Shop> getUserShops(long shopKeeperId);

    Shop getShop(long shopId);
}
