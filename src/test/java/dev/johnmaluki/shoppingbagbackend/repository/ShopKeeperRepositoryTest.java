package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Shop;
import dev.johnmaluki.shoppingbagbackend.entity.ShopKeeper;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopKeeperRepositoryTest {
    @Autowired
    private ShopKeeperRepository shopKeeperRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();
        userRepository.save(user);
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shopkeeper record")
    public void saveShopkeeperTest(){
        ShopKeeper shopKeeper = ShopKeeper.builder()
                .build();

        ShopKeeper saved = shopKeeperRepository.save(shopKeeper);

        assertThat(saved.getId()).isEqualTo(1l);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shopkeeper record with shop")
    public void saveShopkeeperWithShopsTest(){
        Shop shop1 = Shop.builder()
                .name("Electronic1 & Computer Dealer")
                .description("Number 1 seller")
                .isActivated(true)
                .isOpen(true)
                .build();
        Shop shop2 = Shop.builder()
                .name("Electronic2 & Computer Dealer")
                .description("electronic seller")
                .isActivated(true)
                .isOpen(true)
                .build();
        // TODO User savedUser = userRepository.findById(1l).get();
        ShopKeeper shopKeeper = ShopKeeper.builder()
                .build();
        shop1.setShopKeeper(shopKeeper);
        shop2.setShopKeeper(shopKeeper);
        shopRepository.saveAll(List.of(shop1, shop2));


        final long shopkeeperId= 2l;
        List<Shop> shops = shopRepository.findShopkeeperShops(shopkeeperId);

        assertThat(shops).hasSize(2);

    }

    @Transactional
    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shopkeeper record")
    public void deleteShopkeeperTest(){
        // TODO - Enable deletion by the user id
        final long shopkeeperId = 2l;
        final String errorMessage = "Shopkeeper record not found!";
        shopKeeperRepository.deleteById(shopkeeperId);

        List<Shop> shops = shopRepository.findShopkeeperShops(shopkeeperId);
        Throwable thrownException = catchThrowable(()-> {
            shopKeeperRepository.findById(shopkeeperId).orElseThrow(
                    ()-> new NotFoundException(errorMessage)
            );
        });

        assertThat(thrownException).isInstanceOf(NotFoundException.class);
        assertThat(thrownException).hasMessage(errorMessage);
        assertThat(shops).hasSize(0);
    }

}