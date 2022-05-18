package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.TrashBucket;
import dev.johnmaluki.shoppingbagbackend.repository.ShoppingBagRepository;
import dev.johnmaluki.shoppingbagbackend.repository.TrashBucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrashBucketServiceImpl implements TrashBucketService{
    private final TrashBucketRepository trashBucketRepository;
    private final ShoppingBagRepository shoppingBagRepository;

    @Autowired
    public TrashBucketServiceImpl(
            TrashBucketRepository trashBucketRepository,
            ShoppingBagRepository shoppingBagRepository
    ) {
        this.trashBucketRepository = trashBucketRepository;
        this.shoppingBagRepository = shoppingBagRepository;
    }

    @Override
    public TrashBucket trashShoppingBag(long shoppingBagId) {
        boolean itExists = shoppingBagRepository.existsById(shoppingBagId);
        boolean isTrashed = trashBucketRepository
                .existsTrashBucketByShoppingBagShoppingBagId(shoppingBagId);

        if(itExists && !(isTrashed)){
            ShoppingBag shoppingBag = shoppingBagRepository.getShoppingBagById(shoppingBagId);
            TrashBucket trashBucket = TrashBucket.builder()
                    .shoppingBag(shoppingBag)
                    .build();

            return trashBucketRepository.save(trashBucket);
        }
        return null;
    }
}
