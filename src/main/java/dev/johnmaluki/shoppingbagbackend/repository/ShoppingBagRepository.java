package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingBagRepository extends JpaRepository<ShoppingBag, Long> {
    List<ShoppingBag> findByOwnerId(long userId);
}
