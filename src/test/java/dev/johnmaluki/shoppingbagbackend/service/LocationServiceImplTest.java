package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class LocationServiceImplTest {
    @MockBean
    private LocationRepository locationRepository;
    private Location location;

    @BeforeEach
    void setUp() {
        Shop shop = Shop.builder()
                .id(1l)
                .name("Electronic & Computer Dealer")
                .build();

        location = Location.builder()
                .id(1l)
                .town("Mombasa")
                .shop(shop)
                .build();
    }

    @Test
    @DisplayName("[Service] Test fetching shop location")
    void getShopLocation() {
        final long shopId = 1l;
        Mockito.when(locationRepository.findLocationByShopId(shopId))
                .thenReturn(Optional.of(location));

        Optional<Location> shopLocation = locationRepository.findLocationByShopId(shopId);

        assertThat(shopLocation).isPresent();
        assertThat(shopLocation).get().isEqualTo(location);
        verify(locationRepository, times(1))
                .findLocationByShopId(shopId);
    }

    @Test
    @DisplayName("[Service] Test fetching shop location where it does not exist")
    void getShopWithoutLocation() {
        final long shopId = 2l;
        final String errorMessage = "Location does not exist!";
        Mockito.when(locationRepository.findLocationByShopId(shopId))
                .thenThrow(new NotFoundException(errorMessage));

        Throwable notFoundException = catchThrowable(() -> {
            locationRepository.findLocationByShopId(shopId);
        });

        assertThat(notFoundException).isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
        verify(locationRepository, times(1))
                .findLocationByShopId(shopId);
    }
}