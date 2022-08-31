package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Category;
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
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving category record")
    public void saveCategoryTest(){
        final String categoryName = "Laptops".toUpperCase();
        Category category = Category.builder()
                .name(categoryName)
                .build();
        categoryRepository.save(category);

        final long categoryId = 1l;
        Category savedCategory = categoryRepository.findById(categoryId).get();

        assertThat(savedCategory.getId()).isEqualTo(categoryId);
        assertThat(savedCategory.getName()).isEqualTo(categoryName);

    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating category record")
    public void updateCategoryTest(){
        final long categoryId = 1l;
        Category savedCategory = categoryRepository.findById(categoryId).get();
        final String updatedCategoryName = "Laptops and Phones".toUpperCase();
        savedCategory.setName(updatedCategoryName);
        categoryRepository.save(savedCategory);

        Category updatedSavedCategory = categoryRepository.findById(categoryId).get();

        assertThat(updatedSavedCategory.getId()).isEqualTo(categoryId);
        assertThat(updatedSavedCategory.getName()).isEqualTo(updatedCategoryName);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test find all category records")
    public void findAllCategoryTest(){
        final long categoryId = 1l;
        final String categoryName = "Laptops and Phones".toUpperCase();

        List<Category> savedCategories = categoryRepository.findAll();

        assertThat(savedCategories).hasSize(1);
        assertThat(savedCategories.get(0).getId()).isEqualTo(categoryId);
        assertThat(savedCategories.get(0).getName()).isEqualTo(categoryName);
    }
}