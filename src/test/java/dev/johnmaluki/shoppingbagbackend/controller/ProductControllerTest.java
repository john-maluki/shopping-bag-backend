package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipalDetailService;
import dev.johnmaluki.shoppingbagbackend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    UserPrincipal userPrincipal;

    @MockBean
    UserPrincipalDetailService userPrincipalDetailService;

    @MockBean
    UserRepository userRepository;

    Product product;

    @BeforeEach
    void setUp() {
        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        final long productId = 1L;
        product = Product.builder()
                .id(productId)
                .name(productName)
                .description(productDescription)
                .build();
    }

    @Test
    @Disabled
    @DisplayName("[Controller] Test fetching all products")
    void getSystemProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(product);
        Mockito.when(
                productService.getSystemProducts()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("[Controller] Test fetching product")
    void getProductById() throws Exception {
        Mockito.when(productService.getProductById(1L))
                .thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[Controller] Test fetching product with wrong id")
    void getProductByIdException() throws Exception {
        final long productId = 2L;
        Mockito.when(productService.getProductById(2L))
                .thenThrow(new NotFoundException("Product with id {%d} not found".formatted(productId)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}