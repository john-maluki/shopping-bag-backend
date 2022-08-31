package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipalDetailService;
import dev.johnmaluki.shoppingbagbackend.service.ShopProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(ShopProductController.class)
class ShopProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopProductService shopProductService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserPrincipal userPrincipal;

    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @MockBean
    private UserRepository userRepository;

    List<ShopProduct> shopProducts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ShopContact shopContact = ShopContact.builder()
                .contact("0111111111")
                .build();
        Shop shop = Shop.builder()
                .name("Electronic & Computer Dealer")
                .description("Number 1 electronic seller")
                .shopContact(shopContact)
                .isActivated(true)
                .isOpen(true)
                .build();

        final String productName = "HP 440";
        final String productDescription = "4gb ram and 500gb disk space";
        Product product = Product.builder()
                .name(productName)
                .description(productDescription)
                .build();

        final BigDecimal price = BigDecimal.valueOf(500);
        ShopProduct shopProduct = ShopProduct.builder()
                .product(product)
                .shop(shop)
                .price(price)
                .build();
        shopProducts.add(shopProduct);
    }

    @Test
    @Disabled
    @DisplayName("[Controller] Test getting shop products by shop_id")
    void getShopProducts() throws Exception {
        final long shopId = 1l;
        Mockito.when(shopProductService.getShopProducts(shopId))
                .thenReturn(shopProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/shop_products/{id}", shopId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}