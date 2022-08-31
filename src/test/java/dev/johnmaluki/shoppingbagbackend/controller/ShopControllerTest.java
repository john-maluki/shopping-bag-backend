package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipalDetailService;
import dev.johnmaluki.shoppingbagbackend.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
@WebMvcTest(ShopController.class)
class ShopControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ShopService shopService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserPrincipal userPrincipal;

    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @MockBean
    private UserRepository userRepository;

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
    @DisplayName("[Controller] Test getting all shops")
    void getShops() throws Exception {
        List<Shop> shops = List.of(shop);
        Mockito.when(shopService.getShops()).thenReturn(shops);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    @Test
    @DisplayName("[Controller] Test getting all shopkeeper shops with id")
    void testGetShops() throws Exception {
        final long shopKeeperId = 1l;
        List<Shop> shops = List.of(shop);
        Mockito.when(shopService.getUserShops(shopKeeperId)).thenReturn(shops);

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/v1/shops/sk/{id}", shopKeeperId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }
}