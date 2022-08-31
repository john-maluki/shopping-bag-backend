package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShopKeeper;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopKeeperRepository shopKeeperRepository;

    @Autowired
    private UserTrashRepository userTrashRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving user record")
    public void saveUserTest(){
        User user = User.builder()
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();
        userRepository.save(user);

        User savedUser = userRepository.findById(1l).get();

        assertThat(savedUser.getId()).isEqualTo(1l);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching all user records")
    public void findAllUsersTest(){
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(1);
    }

    @Test
    @Order(3)
    @DisplayName("[Repository] Test fetching user record by id")
    public void findUserByIdTest(){
        long userId = 1l;

        User savedUser = userRepository.findById(userId).get();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(savedUser.getId()).isEqualTo(userId);
        softAssertions.assertThat(savedUser.getFirstName())
                .isEqualToIgnoringCase("john");
        softAssertions.assertThat(savedUser.getLastName())
                .isEqualToIgnoringCase("doe");
        softAssertions.assertThat(savedUser.getEmail())
                .isEqualToIgnoringCase("johnDoe@test.com");
        softAssertions.assertAll();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating user record")
    public void updateUserTest(){
        long userId = 1l;
        User savedUser = userRepository.findById(userId).get();
        savedUser.setFirstName("john1");
        savedUser.setLastName("doe1");
        userRepository.save(savedUser);

        savedUser = userRepository.findById(userId).get();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(savedUser.getId()).isEqualTo(userId);
        softAssertions.assertThat(savedUser.getFirstName())
                .isEqualToIgnoringCase("john1");
        softAssertions.assertThat(savedUser.getLastName())
                .isEqualToIgnoringCase("doe1");
        softAssertions.assertThat(savedUser.getEmail())
                .isEqualToIgnoringCase("johnDoe@test.com");
        softAssertions.assertAll();
    }

    @Test
    @Modifying
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating user record with associated shopkeeper")
    public void addUserAsShopkeeperTest(){
        long userId = 1l;
        User savedUser = userRepository.findById(userId).get();
        ShopKeeper shopKeeper = ShopKeeper.builder()
                .user(savedUser)
                .build();

        shopKeeperRepository.save(shopKeeper);
        shopKeeper = shopKeeperRepository.findById(1l).get();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(shopKeeper.getUser().getId()).isEqualTo(1l);
        softAssertions.assertThat(shopKeeper.getUser().getFirstName()).isEqualToIgnoringCase("john1");
        softAssertions.assertAll();
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating user with trash record")
    public void updateUserWithTrashTest(){
        final long userId = 1l;
        User savedUser = userRepository.findById(userId).get();
        UserTrash userTrash = UserTrash.builder()
                .user(savedUser)
                .build();
        userTrashRepository.save(userTrash);

        final long userTrashId = 1l;
        UserTrash updatedUserTrash = userTrashRepository.findUserTrashByUserId(userId).get();

        assertThat(updatedUserTrash.getUser().getId()).isEqualTo(userId);
        assertThat(updatedUserTrash.getUser()).isEqualTo(savedUser);
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting user record")
    public void deleteUserTest(){
        long userId = 1l;

        User savedUser = userRepository.findById(userId).get();
        userRepository.delete(savedUser);

        Throwable userException = catchThrowable(() -> {
            userRepository.findById(userId).get();
        });

        // Assert cascade; on deleting user record
        // should also delete its associated shopkeeper record
        Throwable userShopkeeperException = catchThrowable(() -> {
            shopKeeperRepository.findShopkeeperByUserId(userId).get();
        });

        // Assert cascade; on deleting user record
        // should also delete its associated user trash record
        Throwable userTrashException = catchThrowable(() -> {
            userTrashRepository.findUserTrashByUserId(userId).get();
        });

        assertThat(userException).isInstanceOf(NoSuchElementException.class);
        assertThat(userShopkeeperException).isInstanceOf(NoSuchElementException.class);
        assertThat(userTrashException).isInstanceOf(NoSuchElementException.class);

    }

}