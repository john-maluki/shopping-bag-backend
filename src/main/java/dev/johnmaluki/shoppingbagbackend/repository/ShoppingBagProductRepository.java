package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBagProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingBagProductRepository extends JpaRepository<ShoppingBagProduct, Long> {
    List<ShoppingBagProduct> findAllByShoppingBag(ShoppingBag shoppingBag);

    void deleteByShoppingBagAndShopProduct(ShoppingBag shoppingBag, ShopProduct shopProduct);

    Optional<ShoppingBagProduct> findByShoppingBagAndShopProduct(ShoppingBag shoppingBag, ShopProduct shopProduct);
}
