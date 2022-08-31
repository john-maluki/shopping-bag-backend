package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;

import java.util.List;

public interface ShoppingBagService {
    List<ShoppingBag> getUserShoppingBags(long userId);
    ShoppingBag getShoppingBag(long shoppingBagId);
    boolean trashShoppingBag(long userId, long shoppingBagId);
    List<ShoppingBag> getUserTrashedShoppingBags(long userId);
    boolean undoTrashedShoppingBag(long userId, long shoppingBagId);
}
