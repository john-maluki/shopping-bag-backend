package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShopKeeper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopKeeperRepository extends JpaRepository<ShopKeeper, Long> {
    @Query("SELECT sp FROM ShopKeeper sp WHERE sp.user.id = :userId")
    Optional<ShopKeeper> findShopkeeperByUserId(long userId);
}
