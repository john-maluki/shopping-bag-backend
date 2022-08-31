package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Brand;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandRepositoryTest {
    @Autowired
    BrandRepository brandRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving brand record")
    public void saveBrandTest(){
        final String brandName = "Hp".toUpperCase();
        Brand brand = Brand.builder()
                .name(brandName)
                .build();
        brandRepository.save(brand);

        final long brandId = 1L;
        Brand savedBrand = brandRepository.findById(brandId).get();

        assertThat(savedBrand.getId()).isEqualTo(1);
        assertThat(savedBrand.getName()).isEqualTo(brandName);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test violating brand record uniqueness")
    public void violateBrandUniquenessTest(){
        final String brandName = "Hp".toUpperCase();
        final String errorMessage = "Brand with that name already available!";

        boolean exists = brandRepository.existsByName(brandName);

        assertThat(exists).as(errorMessage).isEqualTo(true);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching brand record by name")
    public void fetchingBrandByNameTest(){
        final String brandName = "Hp".toUpperCase();

        Brand updatedBrand = brandRepository
                .findByBrandName(brandName).get();

        assertThat(updatedBrand.getName()).isEqualTo(brandName);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating brand name record")
    public void updateBrandNameTest(){
        final String brandName = "Hp".toUpperCase();
        final String updatedBrandName = "Lenovo".toUpperCase();
        Brand savedBrand = brandRepository.findByBrandName(brandName).get();
        savedBrand.setName(updatedBrandName);
        brandRepository.save(savedBrand);

        Brand updatedBrand = brandRepository.findByBrandName(updatedBrandName).get();

        assertThat(updatedBrand.getName()).isEqualTo(updatedBrandName);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting brand name record by name")
    public void deleteBrandByNameTest(){
        final String brandName = "Lenovo".toUpperCase();
        final String errorMessage = "Brand record with "+ brandName + " name not found!";
        brandRepository.deleteByBrandName(brandName);


        Throwable brandException = catchThrowable(() -> {
            brandRepository.findByBrandName(brandName).orElseThrow(
                    () -> new NotFoundException(errorMessage)
            );
        });

        assertThat(brandException).isInstanceOf(NotFoundException.class);
        assertThat(brandException).hasMessage(errorMessage);
    }

}