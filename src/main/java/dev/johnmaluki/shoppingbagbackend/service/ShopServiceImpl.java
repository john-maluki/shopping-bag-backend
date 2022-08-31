package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService{
    private final ShopRepository shopRepository;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> getShops() {
        return shopRepository.findAll();
    }

    @Override
    public List<Shop> getUserShops(long shopKeeperId) {
        return shopRepository.findShopkeeperShops(shopKeeperId);
    }

    @Override
    public Shop getShop(long shopId) {
        return shopRepository.findById(shopId).orElseThrow(
                () -> new NotFoundException("Shop not found!")
        );
    }
}
