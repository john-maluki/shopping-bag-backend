package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTrashRepositoryTest {
    @Autowired
    private UserTrashRepository userTrashRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving user trash record")
    public void saveUserTrashTest(){
        final String email = "johnDoe@test.com";
        User user = User.builder()
                .email(email)
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();
        UserTrash userTrash = UserTrash.builder()
                .user(user)
                .build();
        userTrashRepository.save(userTrash);

        final long userId = 1l; // no other user created
        UserTrash savedUserTrash = userTrashRepository.findUserTrashByUserId(userId).get();

        assertThat(savedUserTrash.getId()).isEqualTo(1l);
        assertThat(savedUserTrash.getUser().getId()).isEqualTo(userId);
        assertThat(savedUserTrash.getUser().getEmail()).isEqualToIgnoringCase(email);

    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching user trash by user id")
    void findUserTrash() {
        final long userId = 1l;
        final String email = "johnDoe@test.com";

        UserTrash savedUserTrash = userTrashRepository.findUserTrashByUserId(userId).get();

        assertThat(savedUserTrash.getId()).isEqualTo(1l);
        assertThat(savedUserTrash.getUser().getId()).isEqualTo(userId);
        assertThat(savedUserTrash.getUser().getEmail()).isEqualToIgnoringCase(email);
    }

}