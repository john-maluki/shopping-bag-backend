package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrashBucketRepository extends JpaRepository<TrashBucket, Long> {
    @Query("SELECT tb FROM TrashBucket tb WHERE tb.userTrash.id = :userTrashId")
    List<TrashBucket> findAllTrashBucketByUserTrashId(long userTrashId);

    @Query("SELECT tb.shoppingBag FROM TrashBucket tb WHERE tb.userTrash.id = :userTrashId AND tb.shoppingBag.id = :shoppingBagId")
    Optional<ShoppingBag> findShoppingBagByUserTrashId(long userTrashId, long shoppingBagId);

    void deleteByUserTrashIdAndShoppingBagId(long userTrashId, long shoppingBagId);

    boolean existsByShoppingBag(ShoppingBag shoppingBag);
}
