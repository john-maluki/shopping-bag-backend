package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Location;
import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopContact;
import dev.johnmaluki.shoppingbagbackend.entity.ShopKeeper;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopRepositoryTest {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShopKeeperRepository shopKeeperRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shop record")
    public void saveShopTest(){
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
        shopRepository.save(shop);

        long shopId = 1l;
        List<Shop> savedShops = shopRepository.findAll();

        assertThat(savedShops.size()).isEqualTo(1);
        assertThat(savedShops.get(0).getId()).isEqualTo(shopId);
        assertThat(savedShops.get(0).getShopContact().getContact()).isEqualTo("0111111111");
        assertThat(savedShops.get(0).getName()).isEqualToIgnoringCase("Electronic & Computer Dealer");
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching shop record by id")
    public void fetchShopByIdTest(){
        long shopId = 1l;

        Shop savedShop = shopRepository.getById(shopId);

        assertThat(savedShop.getId()).isEqualTo(shopId);
        assertThat(savedShop.getShopContact().getContact()).isEqualTo("0111111111");
        assertThat(savedShop.getName()).isEqualToIgnoringCase("Electronic & Computer Dealer");
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating shop record by id")
    public void updateShopByIdTest(){
        long shopId = 1l;
        Shop savedShop = shopRepository.getById(shopId);

        ShopContact newShopContact = ShopContact.builder()
                .contact("0111111122")
                .build();
        savedShop.setShopContact(newShopContact);
        savedShop.setName("Electronic & Computer");


        assertThat(savedShop.getId()).isEqualTo(shopId);
        assertThat(savedShop.getShopContact().getContact()).isEqualTo("0111111122");
        assertThat(savedShop.getName()).isEqualToIgnoringCase("Electronic & Computer");
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding all shop records")
    public void finAllShopsTest(){
        long shopId = 1l;

        List<Shop> savedShops = shopRepository.findAll();

        assertThat(savedShops.size()).isEqualTo(1);
        assertThat(savedShops.get(0).getId()).isEqualTo(shopId);
        assertThat(savedShops.get(0).getShopContact().getContact()).isEqualTo("0111111122");
        assertThat(savedShops.get(0).getName()).isEqualToIgnoringCase("Electronic & Computer");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating shop with location record")
    public void updatingShopWithLocationTest(){
        long shopId = 1l;
        Location location = Location.builder()
                .town("Nairobi")
                .description("Moi Avenue, John Building")
                .state("Kenya")
                .street("Moi Avenue")
                .build();
        Shop savedShop = shopRepository.findById(shopId).get();
        location.setShop(savedShop);
        locationRepository.save(location);

        Shop updatedSavedShop = shopRepository.findById(shopId).get();
        long locationId = 1l;
        Location savedLocation = locationRepository.findById(locationId).get();

        assertThat(savedLocation.getShop().getId()).isEqualTo(shopId);
        assertThat(savedLocation.getShop()).isEqualTo(updatedSavedShop);

    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shop record with shopkeeper")
    public void saveShopWithShopkeeperTest(){
        ShopKeeper shopKeeper = ShopKeeper.builder()
                .build();
        final String shopName = "Electronic2 & Computer Dealer";
        Shop shop = Shop.builder()
                .name(shopName)
                .description("electronic seller")
                .shopKeeper(shopKeeper)
                .isActivated(true)
                .isOpen(true)
                .build();
        shopRepository.save(shop);

        final long shopId = 2l;
        Shop savedShop = shopRepository.findById(shopId).get();

        assertThat(savedShop.getId()).isEqualTo(shopId);
        assertThat(savedShop.getName()).isEqualTo(shopName);
        assertThat(savedShop.getShopKeeper().getId()).isEqualTo(1l);
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding all shop records by shopkeeper id")
    public void findShopkeeperShopTest(){
        final long shopkeeperId = 1l;
        final long shopId = 2l;
        final String shopName = "Electronic2 & Computer Dealer";

        List<Shop> shopkeeperShops = shopRepository.findShopkeeperShops(shopkeeperId);

        assertThat(shopkeeperShops).hasSize(1);
        assertThat(shopkeeperShops.get(0).getId()).isEqualTo(shopId);
        assertThat(shopkeeperShops.get(0).getName()).isEqualToIgnoringCase(shopName);
    }

    @Test
    @Order(8)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shop record by id")
    public void deleteShopByIdTest(){
        long shopId = 1l;

        shopRepository.deleteById(shopId);

        Throwable thrownByShop = catchThrowable(
                ()-> shopRepository.findById(shopId)
                        .orElseThrow(()-> new NotFoundException("Shop not found!")));

        // Assert cascade; on deleting shop record
        // should also delete its associated location record
        Throwable thrownByLocation = catchThrowable(
                ()-> locationRepository.findLocationByShopId(shopId)
                        .orElseThrow(()-> new NotFoundException("Location not found!")));

        assertThat(thrownByShop).as("ShopThrownException should not be null")
                .isNotNull();
        assertThat(thrownByLocation).as("LocationThrownException should not be null")
                .isNotNull();
        assertThat(thrownByShop).isInstanceOf(NotFoundException.class)
                .hasMessage("Shop not found!");
        assertThat(thrownByLocation).isInstanceOf(NotFoundException.class)
                .hasMessage("Location not found!");
    }

}