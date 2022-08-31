package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Location;

public interface LocationService {
    Location getShopLocation(long shopId);
}
