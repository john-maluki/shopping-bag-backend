package dev.johnmaluki.shoppingbagbackend.controller;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipalDetailService;
import dev.johnmaluki.shoppingbagbackend.service.ShoppingBagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(ShoppingBagController.class)
class ShoppingBagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingBagService shoppingBagService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserPrincipal userPrincipal;

    @MockBean
    private UserPrincipalDetailService userPrincipalDetailService;

    @MockBean
    private UserRepository userRepository;

    private ShoppingBag shoppingBag;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();

        shoppingBag = ShoppingBag.builder()
                .dateCreated(LocalDateTime.now())
                .owner(user)
                .shoppingProcessStatus(ShoppingBag.ShoppingProcessStatus.ONGOING)
                .build();
    }

    @Test
    @DisplayName("[Controller] Test getting user shopping_bags")
    @WithMockUser(roles = "USER")
    void getUserShoppingBags() throws Exception {
        final long userId = 1l;
        Mockito.when(shoppingBagService.getUserShoppingBags(userId))
                .thenReturn(List.of(shoppingBag));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/shopping_bag/user_shopping_bags/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[Controller] Test getting any shopping_bag by id")
    @WithMockUser(roles = "USER")
    void getShoppingBag() throws Exception {
        final long shoppingBagId = 1l;
        Mockito.when(shoppingBagService.getShoppingBag(shoppingBagId))
                .thenReturn(shoppingBag);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/shopping_bag/{id}", shoppingBagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[Controller] Test saving shopping_bag in trash_bucket successful")
    @WithMockUser(roles = "USER")
    void trashShoppingBagSuccessful() throws Exception {
        Mockito.when(shoppingBagService.trashShoppingBag(1l, 1l))
                        .thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/shopping_bag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"userId\": 1,\n" +
                                "    \"shoppingBagId\": 1\n" +
                                "   \n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @DisplayName("[Controller] Test saving shopping_bag in trash_bucket unsuccessful")
    @WithMockUser(roles = "USER")
    void trashShoppingBagUnSuccessful() throws Exception {
        Mockito.when(shoppingBagService.trashShoppingBag(1l, 1l))
                .thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/shopping_bag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"userId\": 1,\n" +
                                "    \"shoppingBagId\": 1\n" +
                                "   \n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("[Controller] Test getting any shopping_bag by wrong id")
    @WithMockUser(roles = "USER")
    void getShoppingBagByWrongId() throws Exception {
        final long shoppingBagId = 2l;
        final String errorMessage = "ShoppingBag not found!";
        Mockito.when(shoppingBagService.getShoppingBag(shoppingBagId))
                .thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/shopping_bag/{id}", shoppingBagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}