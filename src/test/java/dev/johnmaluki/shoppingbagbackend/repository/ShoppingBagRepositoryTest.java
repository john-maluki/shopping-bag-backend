package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShoppingBagRepositoryTest {
    @Autowired
    private ShoppingBagRepository shoppingBagRepository;
    @Autowired
    private UserTrashRepository userTrashRepository;
    @Autowired
    private TrashBucketRepository trashBucketRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving shopping_bag record")
    public void saveShopBagTest(){
        User user = User.builder()
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();

        ShoppingBag shoppingBag = ShoppingBag.builder()
                .dateCreated(LocalDateTime.now())
                .owner(user)
                .shoppingProcessStatus(ShoppingBag.ShoppingProcessStatus.ONGOING)
                .build();
        shoppingBagRepository.save(shoppingBag);
        
        final long shoppingBagId = 1l;
        ShoppingBag savedBag = shoppingBagRepository.findById(shoppingBagId).orElse(null);
        
        assertThat(savedBag).as("Should not return null").isNotNull();
        assertThat(savedBag.getId()).isEqualTo(shoppingBagId);
        assertThat(savedBag.getDateCreated()).isNotNull();
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding shopping_bag records by owner id")
    void findByOwnerId() {
        final long userId = 1l;

        List<ShoppingBag> shoppingBags = shoppingBagRepository
                .findByOwnerId(userId);

        assertThat(shoppingBags)
                .as("Should not be empty")
                .isNotEmpty();
        assertThat(shoppingBags).hasSize(1);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test updating shopping_bag record on shopping process status")
    public void updateShopBagProcessStateTest(){
        final long shoppingBagId = 1l;
        ShoppingBag savedBag = shoppingBagRepository.findById(shoppingBagId).get();
        savedBag.setShoppingProcessStatus(ShoppingBag.ShoppingProcessStatus.COMPLETE);
        shoppingBagRepository.save(savedBag);


        ShoppingBag updatedSavedBag = shoppingBagRepository.findById(shoppingBagId).get();

        assertThat(updatedSavedBag.getShoppingProcessStatus())
                .isEqualTo(ShoppingBag.ShoppingProcessStatus.COMPLETE);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shopping_bag record")
    public void deleteShopBagTest(){
        final long shoppingBagId = 1l;
        ShoppingBag savedBag = shoppingBagRepository.findById(shoppingBagId).get();
        UserTrash userTrash = createUserTrash(savedBag.getOwner());
        TrashBucket trashBucket = trashShoppingBag(userTrash, savedBag);
        System.out.println("trashBucket = " + trashBucket);
        trashBucketRepository.deleteByUserTrashIdAndShoppingBagId(
                userTrash.getId(), savedBag.getId()
        );
        shoppingBagRepository.delete(savedBag);

        final String bagErrorMessage = "ShoppingBag not found!";
        Throwable shoppingBagException = catchThrowable(() -> {
            shoppingBagRepository.findById(shoppingBagId).orElseThrow(
                    ()-> new NotFoundException(bagErrorMessage)
            );
        });

        final String bucketErrorMessage = "ShoppingBag not found!";
        Throwable trashBucketException = catchThrowable(() -> {
            trashBucketRepository.findById(trashBucket.getId()).orElseThrow(
                    ()-> new NotFoundException(bucketErrorMessage)
            );
        });

        assertThat(shoppingBagException).isInstanceOf(NotFoundException.class)
                .hasMessage(bagErrorMessage);
        assertThat(trashBucketException).isInstanceOf(NotFoundException.class)
                .hasMessage(bucketErrorMessage);

    }

    private TrashBucket trashShoppingBag(UserTrash userTrash, ShoppingBag shoppingBag) {
        TrashBucket trashBucket = TrashBucket.builder()
                .shoppingBag(shoppingBag)
                .userTrash(userTrash)
                .build();
        return trashBucketRepository.save(trashBucket);
    }

    private UserTrash createUserTrash(User user) {
        UserTrash userTrash = UserTrash.builder()
                .user(user)
                .build();
        return userTrashRepository.save(userTrash);
    }
}