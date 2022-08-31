package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;

import java.util.List;

public interface TrashBucketService {
    List<TrashBucket> getUserTrashedShoppingBags(long userTrashId);
    TrashBucket trashShoppingBag(long shoppingBagId);
    void deleteShoppingBagPermanently(long trashBucketId);
}
