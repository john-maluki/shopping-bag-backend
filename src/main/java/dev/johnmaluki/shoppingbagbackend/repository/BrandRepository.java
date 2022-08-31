package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    @Query("SELECT b FROM Brand b WHERE b.name = :brandName")
    Optional<Brand> findByBrandName(String brandName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Brand b WHERE b.name = :brandName")
    void deleteByBrandName(String brandName);
}
