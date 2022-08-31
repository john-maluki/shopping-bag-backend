package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTrashRepository extends JpaRepository<UserTrash, Long> {
    @Query("SELECT ut FROM UserTrash ut WHERE ut.user.id = :userId")
    Optional<UserTrash> findUserTrashByUserId(long userId);
}
