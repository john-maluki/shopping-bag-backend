package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipalDetailService;
import dev.johnmaluki.shoppingbagbackend.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(LocationController.class)
class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private UserPrincipal userPrincipal;

    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper modelMapper;

    Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1l)
                .town("Mombasa")
                .build();
    }

    @Test
    @DisplayName("[Controller] Test getting shop location")
    void getShopLocation() throws Exception {
        final long shopId = 1l;
        Mockito.when(locationService.getShopLocation(shopId))
                .thenReturn(location);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/shop_location/{id}", shopId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[Controller] Test getting shop location where shop_location does exist ")
    void getShopLocationWithoutLocation() throws Exception {
        final long shopId = 2l;
        final String errorMessage = "Location does not exist!";
        Mockito.when(locationService.getShopLocation(shopId))
                .thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/shop_location/{id}", shopId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}