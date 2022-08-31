package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShopServiceImplTest {
    @MockBean
    private ShopRepository shopRepository;

    Shop shop;

    @BeforeEach
    void setUp() {
        ShopContact shopContact = ShopContact.builder()
                .contact("0111111111")
                .build();
       shop = Shop.builder()
                .name("Electronic & Computer Dealer")
                .description("Number 1 electronic seller")
                .shopContact(shopContact)
                .isActivated(true)
                .isOpen(true)
                .build();
    }

    @Test
    @DisplayName("[Service] Test fetching all shops")
    void getShops() {
        Mockito.when(shopRepository.findAll()).thenReturn(List.of(shop));

        List<Shop> shops = shopRepository.findAll();

        assertThat(shops).hasSize(1);

    }

    @Test
    @DisplayName("[Service] Test fetching all shopkeeper shops by id")
    void getUserShops() {
        final long shopKeeperId = 1l;
        Mockito.when(shopRepository.findShopkeeperShops(shopKeeperId))
                .thenReturn(List.of(shop));

        List<Shop> shops = shopRepository.findShopkeeperShops(shopKeeperId);

        assertThat(shops).hasSize(1);
    }
}