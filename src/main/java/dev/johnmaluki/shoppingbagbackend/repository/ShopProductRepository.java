package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
    @Query("SELECT sp FROM ShopProduct sp WHERE sp.shop.id = :shopId")
    List<ShopProduct> findShopProductsByShopId(long shopId);
    @Query("SELECT sp FROM ShopProduct sp WHERE sp.shop.id = :shopId AND sp.product.id = :productId")
    Optional<ShopProduct> findShopProductsByShopIdAndProductIdTest(long shopId, long productId);
    @Transactional
    @Modifying
    @Query("DELETE FROM ShopProduct sp WHERE sp.shop.id = :shopId AND sp.product.id = :productId")
    void deleteShopProductByShopIdAndProductId(long shopId, long productId);;

    boolean existsByProduct(Product product);
}
