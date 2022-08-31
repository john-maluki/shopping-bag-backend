package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.ProductRepository;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceImplTest {
    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        final long productId = 1l;
        Product product = Product.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .build();
        Mockito.when(productRepository.getProductById(productId)).thenReturn(Optional.of(product));
    }

    @Test
    @DisplayName("[Service] Test fetching all system products")
    void getSystemProducts() {
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        final long productId = 1l;
        Product product = Product.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .build();
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product>  products = productRepository.findAll();

        assertThat(products).hasSize(1);
        assertThat(products).contains(product);
    }

    @Test
    @DisplayName("[Service] Test fetching product by given id")
    void getProductById() {
        final long productId = 1l;

        Optional<Product> product = productRepository.getProductById(productId);

        assertThat(product).isPresent();
    }

    @Test
    @DisplayName("[Service] Test fetching product by given id which is not available")
    void getProductByIdWithNotFoundException() {
        final long productId = 2l;
        final String errorMessage = "Product with id {%d} not found".formatted(productId);
        Mockito.when(productRepository.getProductById(productId))
                .thenThrow(new NotFoundException(errorMessage));

        Throwable productNotFoundException = catchThrowable(() -> {
            productRepository.getProductById(productId);
        });

        assertThat(productNotFoundException).isInstanceOf(NotFoundException.class);
        assertThat(productNotFoundException).hasMessage(errorMessage);
    }
}