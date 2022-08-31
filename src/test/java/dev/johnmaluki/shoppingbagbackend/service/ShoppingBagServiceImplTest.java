package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.ShoppingBagRepository;
import dev.johnmaluki.shoppingbagbackend.repository.TrashBucketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ShoppingBagServiceImplTest {
    @MockBean
    private ShoppingBagRepository shoppingBagRepository;
    @MockBean
    private TrashBucketRepository trashBucketRepository;

    private ShoppingBag shoppingBag;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1l)
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();

        shoppingBag = ShoppingBag.builder()
                .id(1l)
                .dateCreated(LocalDateTime.now())
                .owner(user)
                .shoppingProcessStatus(ShoppingBag.ShoppingProcessStatus.ONGOING)
                .build();
    }

    @Test
    @DisplayName("[Service] Test fetching user shopping_bags")
    void getUserShoppingBags() {
        final long userId = 1l;
        Mockito.when(shoppingBagRepository.findByOwnerId(userId))
                .thenReturn(List.of(shoppingBag));

        List<ShoppingBag> shoppingBags = shoppingBagRepository.findByOwnerId(userId);

        verify(shoppingBagRepository, times(1))
                .findByOwnerId(userId);
        assertThat(shoppingBags).hasSize(1);
    }

    @Test
    @DisplayName("[Service] Test fetching shopping_bag by shopping_id")
    void getShoppingBagById() {
        final long shoppingBagId = 1l;
        Mockito.when(shoppingBagRepository.findById(shoppingBagId))
                .thenReturn(Optional.of(shoppingBag));

        Optional<ShoppingBag> obtainedShoppingBag = shoppingBagRepository
                .findById(shoppingBagId);

        verify(shoppingBagRepository, times(1))
                .findById(shoppingBagId);

        assertThat(obtainedShoppingBag).isPresent();
        assertThat(obtainedShoppingBag).get().isEqualTo(shoppingBag);
    }

    @Test
    @DisplayName("[Service] Test fetching shopping_bag by wrong shopping_id")
    void getShoppingBagByWrongId() {
        final long shoppingBagId = 2l; // This id does not exist
        final String errorMessage = "ShoppingBag not found!";
        Mockito.when(shoppingBagRepository.findById(shoppingBagId))
                .thenThrow(new NotFoundException(errorMessage));

        Throwable shoppingBagNotFoundException = catchThrowable(() -> {
            shoppingBagRepository
                    .findById(shoppingBagId);
        });

        assertThat(shoppingBagNotFoundException).isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
        verify(shoppingBagRepository, times(1))
                .findById(shoppingBagId);
    }

    @Test
    @DisplayName("[Service] Test trashing user shopping_bag")
    void trashShoppingBag() {
        final long userId = 1l;
        final long userTrashId = 1l;
        final long shoppingBagId = 1l;
        UserTrash userTrash = UserTrash.builder()
                .id(1l)
                .user(User.builder().id(userId).build())
                .build();
        TrashBucket trashBucket = TrashBucket.builder()
                .userTrash(userTrash)
                .shoppingBag(shoppingBag)
                .build();
        Mockito.when(trashBucketRepository.save(trashBucket))
                .thenReturn(trashBucket);

        trashBucketRepository
                .save(trashBucket);

        verify(trashBucketRepository, times(1))
                .save(trashBucket);
    }
}