package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashBucketRepository extends JpaRepository<TrashBucket, Long> {
    boolean existsTrashBucketByShoppingBagShoppingBagId(long shoppingBagId);
}
