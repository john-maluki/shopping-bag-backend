package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Category;
import dev.johnmaluki.shoppingbagbackend.entity.ProductType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductTypeRepositoryTest {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving product_type record")
    public void saveProductTypeTest(){
        final String categoryName = "Laptops and phones".toUpperCase();
        Category category = Category.builder()
                .name(categoryName)
                .build();
        categoryRepository.save(category);
        final long categoryId = 1l;

        Category savedCategory = categoryRepository.findById(categoryId).get();
        final String productTypeName1 = "Laptops".toUpperCase();
        final String productTypeName2 = "Phones".toUpperCase();
        ProductType productType1 = ProductType.builder()
                .name(productTypeName1)
                .category(savedCategory)
                .build();
        ProductType productType2 = ProductType.builder()
                .name(productTypeName2)
                .category(savedCategory)
                .build();
        productTypeRepository.saveAll(List.of(productType1, productType2));

        List<ProductType> productTypes = productTypeRepository.findAll();

        assertThat(productTypes).hasSize(2);
        assertThat(productTypes).containsAll(List.of(productType1, productType2));
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching all product_type records")
    public void findAllProductTypeTest(){
        List<ProductType> productTypes = productTypeRepository.findAll();

        assertThat(productTypes).hasSize(2);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching all product_type record by category")
    public void findAllProductTypeByCategoryTest(){
        final long categoryId = 1l;
        Category category = categoryRepository.findById(categoryId).get();

        List<ProductType> productTypes = productTypeRepository
                .findAllProductTypesWithCategory(category);

        assertThat(productTypes).hasSize(2);
    }
}