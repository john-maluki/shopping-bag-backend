package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Category;
import dev.johnmaluki.shoppingbagbackend.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    @Query("SELECT pt FROM ProductType pt WHERE pt.category = :category")
    List<ProductType> findAllProductTypesWithCategory(Category category);
}
