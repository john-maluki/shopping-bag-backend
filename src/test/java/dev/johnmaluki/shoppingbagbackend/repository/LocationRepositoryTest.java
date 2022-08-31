package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving location record")
    public void saveLocationTest(){
        Location location = Location.builder()
                .town("Nairobi")
                .description("Moi Avenue, John Building")
                .state("Kenya")
                .street("Moi Avenue")
                .build();
        locationRepository.save(location);

        long locationId = 1l;
        Location savedLocation = locationRepository.getById(locationId);

        assertThat(savedLocation.getId()).isEqualTo(locationId);
        assertThat(savedLocation.getState()).isEqualToIgnoringCase("kenya");
        assertThat(savedLocation.getStreet()).isEqualToIgnoringCase("Moi Avenue");
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching location record by id")
    public void fetchingLocationByTest(){
        long locationId = 1l;

        Location savedLocation = locationRepository.findById(locationId).get();

        assertThat(savedLocation.getId()).isEqualTo(locationId);
        assertThat(savedLocation.getState()).isEqualToIgnoringCase("kenya");
        assertThat(savedLocation.getStreet()).isEqualToIgnoringCase("Moi Avenue");

    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding all location records")
    public void findAllLocationTest(){
        long locationId = 1l;

        List<Location> savedLocations = locationRepository.findAll();

        assertThat(savedLocations.size()).isEqualTo(1);
        assertThat(savedLocations.get(0).getId()).isEqualTo(locationId);
        assertThat(savedLocations.get(0).getState()).isEqualToIgnoringCase("kenya");
        assertThat(savedLocations.get(0).getStreet()).isEqualToIgnoringCase("Moi Avenue");
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating location record")
    public void updateLocationTest(){
        long locationId = 1l;
        Location savedLocation = locationRepository.findById(locationId).get();
        savedLocation.setStreet("Tom Avenue");
        locationRepository.save(savedLocation);

        Location newUpdatedLocation = locationRepository.findById(locationId).get();

        assertThat(newUpdatedLocation.getId()).isEqualTo(locationId);
        assertThat(newUpdatedLocation.getState()).isEqualToIgnoringCase("kenya");
        assertThat(newUpdatedLocation.getStreet()).isEqualToIgnoringCase("Tom Avenue");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving location record with shop")
    public void saveLocationWithShopTest(){
        Shop shop = Shop.builder()
                .name("Electronic & Computer Dealer")
                .build();
        Shop savedShop = shopRepository.save(shop);

        Location location = Location.builder()
                .town("Mombasa")
                .shop(savedShop)
                .build();

        Location savedLocation = locationRepository.save(location);

        assertThat(savedLocation.getId()).isEqualTo(2l);
        assertThat(savedLocation.getShop()).isEqualTo(savedShop);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching shop location record by shop id")
    public void fetchingShopLocationShopIdTest(){
        long shopId = 1l;

        Location savedLocation = locationRepository.findLocationByShopId(shopId).get();

        assertThat(savedLocation.getId()).isEqualTo(2l);
        assertThat(savedLocation.getShop().getName()).isEqualToIgnoringCase("Electronic & Computer Dealer");
    }
}