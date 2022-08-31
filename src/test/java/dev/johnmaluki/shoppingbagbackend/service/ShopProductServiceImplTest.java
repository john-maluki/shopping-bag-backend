package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Product;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.entity.ShopProduct;
import dev.johnmaluki.shoppingbagbackend.repository.ShopProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ShopProductServiceImplTest {
    @MockBean
    private ShopProductRepository shopProductRepository;

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
    @DisplayName("[Service] Test fetching shop_products by given shop_id")
    void getShopProducts() {
        final long shopId = 1l;
        Mockito.when(shopProductRepository.findShopProductsByShopId(shopId))
                .thenReturn(shopProducts);

        List<ShopProduct> shopProducts1 = shopProductRepository
                .findShopProductsByShopId(shopId);

        assertThat(shopProducts1).hasSize(1);
        verify(shopProductRepository, times(1))
                .findShopProductsByShopId(shopId);
    }
}