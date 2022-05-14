package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getSystemProducts();
}
