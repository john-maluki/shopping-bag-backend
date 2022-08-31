package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import dev.johnmaluki.shoppingbagbackend.repository.ShopProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductServiceImpl  implements ShopProductService{
    private final ShopProductRepository shopProductRepository;

    @Autowired
    public ShopProductServiceImpl(ShopProductRepository shopProductRepository) {
        this.shopProductRepository = shopProductRepository;
    }

    @Override
    public List<ShopProduct> getShopProducts(long shopId) {
        List<ShopProduct> shopProducts = shopProductRepository.findShopProductsByShopId(shopId);
        shopProducts.forEach(shopProduct -> shopProduct.getProduct().setImageUrl(obtainProductImageUrl()));
        return shopProducts;
    }

    private String obtainProductImageUrl() {
        return "http://localhost:8080/api/product-images/GkqJXcvMkB-12-1.jpg";
    }
}
