package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrashBucketRepositoryTest {
    @Autowired
    private TrashBucketRepository trashBucketRepository;

    @Autowired
    private ShoppingBagRepository shoppingBagRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving trash bucket record")
    public void saveTrashBucketTest(){
        UserTrash userTrash = UserTrash.builder()
                .build();
        final BigDecimal price = BigDecimal.valueOf(3000);
        ShoppingBag shoppingBag = ShoppingBag.builder()
                .shoppingProcessStatus(ShoppingBag.ShoppingProcessStatus.ONGOING)
                .totalProductPrice(price)
                .build();
        TrashBucket trashBucket = TrashBucket.builder()
                .userTrash(userTrash)
                .shoppingBag(shoppingBag)
                .build();
        trashBucketRepository.save(trashBucket);

        final long trashBucketId = 1l;
        TrashBucket savedTrashBucket = trashBucketRepository.findById(trashBucketId).get();

        assertThat(savedTrashBucket.getId()).isEqualTo(trashBucketId);


    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching all trash bucket records by user trash id")
    void findAllTrashBucketByUserTrashId() {
        final long userTrashId = 1l;

        List<TrashBucket> trashBuckets =  trashBucketRepository
                .findAllTrashBucketByUserTrashId(userTrashId);

        assertThat(trashBuckets).hasSize(1);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test shopping bag trashed successfully")
    void shoppingBagTrashedTest() {
        final long userTrashId = 1l;
        final long shoppingBagId = 1l;

        ShoppingBag shoppingBag =  trashBucketRepository
                .findShoppingBagByUserTrashId(userTrashId, shoppingBagId).get();

        assertThat(shoppingBag.getId()).isEqualTo(shoppingBagId);
        assertThat(shoppingBag.getShoppingProcessStatus())
                .isEqualTo(ShoppingBag.ShoppingProcessStatus.ONGOING);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test shopping_bag exists")
    void existsByShoppingBag() {
        final long userTrashId = 1l;
        final long shoppingId = 1l;
        ShoppingBag shoppingBag =  trashBucketRepository
                .findShoppingBagByUserTrashId(userTrashId, shoppingId).get();

        boolean existsByShoppingBag =  trashBucketRepository
                .existsByShoppingBag(shoppingBag);

        assertThat(existsByShoppingBag).isTrue();

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting shopping_bag record by user_trash_id and shopping_bag_id")
    void deletingTrashedShoppingBagPermanentlyTest() {
        // This operation will undo the shoppingBag
        // It will not appear in trashed buckets
        final long userTrashId = 1l;
        final long shoppingBagId = 1l;
        final String errorMessage = "Trash bucket not found!";
        trashBucketRepository
                .deleteByUserTrashIdAndShoppingBagId(userTrashId, shoppingBagId);

        Throwable deletedBagException = catchThrowable(() -> {
            trashBucketRepository
                    .findShoppingBagByUserTrashId(userTrashId, shoppingBagId).orElseThrow(
                            ()-> new NotFoundException(errorMessage)
                    );
        });

        assertThat(deletedBagException).isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }
}