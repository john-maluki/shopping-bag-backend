package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.*;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.exception.ShoppingBagAlreadyTrashedException;
import dev.johnmaluki.shoppingbagbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShoppingBagServiceImpl implements ShoppingBagService {

    private final ShoppingBagRepository shoppingBagRepository;

    @Autowired
    private TrashBucketRepository trashBucketRepository;

    @Autowired
    private UserTrashRepository userTrashRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingBagProductRepository shoppingBagProductRepository;

    @Autowired
    public ShoppingBagServiceImpl(ShoppingBagRepository shoppingBagRepository) {
        this.shoppingBagRepository = shoppingBagRepository;
    }

    @Override
    public List<ShoppingBag> getUserShoppingBags(long userId) {
        ifUserDoesNotExistThrowExceptionElseReturnUser(userId);

        List<ShoppingBag> shoppingBags = shoppingBagRepository.findByOwnerId(userId);;
        UserTrash userTrash = getUserTrash(userId);
        List<ShoppingBag> trashedShoppingBags = getUserTrashedBags(userTrash);
        shoppingBags = shoppingBags.stream()
                    .filter(shoppingBag -> !(trashedShoppingBags.contains(shoppingBag)))
                    .collect(Collectors.toList());

        return shoppingBags;
    }

    @Override
    public ShoppingBag getShoppingBag(long shoppingBagId) {
        ShoppingBag shoppingBag = getShoppingBagOrThrowExceptionIfDoesNotExist(shoppingBagId);
//        Set<ShoppingBagProduct> shoppingBagProducts = getShoppingBagProducts(shoppingBag);
//        shoppingBag.setShoppingBagProducts(shoppingBagProducts);
        return shoppingBag;
    }

    @Override
    public boolean trashShoppingBag(long userId, long shoppingBagId) {
        ifUserDoesNotExistThrowExceptionElseReturnUser(userId);
        ShoppingBag shoppingBag = getShoppingBag(shoppingBagId);
        ifShoppingBagAlreadyTrashedThrowException(shoppingBag);
        UserTrash userTrash = getUserTrash(userId);
        TrashBucket trashBucket = TrashBucket.builder()
                .userTrash(userTrash)
                .shoppingBag(shoppingBag)
                .build();

       TrashBucket trashedBucket =  trashBucketRepository.save(trashBucket);
       return trashedBucket.getId() > 0;

    }

    @Override
    public List<ShoppingBag> getUserTrashedShoppingBags(long userId) {
        UserTrash userTrash = getUserTrash(userId);
        return getUserTrashedBags(userTrash);
    }
    @Override
    @Transactional
    public boolean undoTrashedShoppingBag(long userId, long shoppingBagId) {
        User user = ifUserDoesNotExistThrowExceptionElseReturnUser(userId);
        ShoppingBag shoppingBag = getShoppingBagOrThrowExceptionIfDoesNotExist(shoppingBagId);
        UserTrash userTrash = getUserTrash(user.getId());
        ifTrashBucketNotFoundThrowException(
                userTrash.getId(),
                shoppingBag.getId()
        );
        trashBucketRepository
                .deleteByUserTrashIdAndShoppingBagId(
                        userTrash.getId(),
                        shoppingBag.getId()
                );
        return !(trashBucketRepository.existsByShoppingBag(
                shoppingBag
        ));
    }

    private List<ShoppingBag> getUserTrashedBags(UserTrash userTrash){
        List<TrashBucket> trashBuckets = trashBucketRepository
                .findAllTrashBucketByUserTrashId(userTrash.getId());
        return trashBuckets.stream().map(TrashBucket::getShoppingBag)
                .collect(Collectors.toList());
    }

    private UserTrash getUserTrash(long userId){
        UserTrash userTrash;
        Optional<UserTrash> userTrashOptional = userTrashRepository
                .findUserTrashByUserId(userId);
        if (userTrashOptional.isPresent()){
            userTrash = userTrashOptional.get();
        } else {
            User user = ifUserDoesNotExistThrowExceptionElseReturnUser(userId);
            userTrash = createUserTrash(user);
        }
       return userTrash;
    }

    private User ifUserDoesNotExistThrowExceptionElseReturnUser(long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    private void ifShoppingBagAlreadyTrashedThrowException(ShoppingBag shoppingBag){
        boolean exists = trashBucketRepository.existsByShoppingBag(shoppingBag);
        if (exists){
            throw new ShoppingBagAlreadyTrashedException("ShoppingBag already trashed");
        }
    }

    private UserTrash createUserTrash(User user){
        UserTrash userTrash =  UserTrash.builder()
                .user(user)
                .build();
        return userTrashRepository.save(userTrash);
    }

    private ShoppingBag getShoppingBagOrThrowExceptionIfDoesNotExist(long shoppingBagId){
       return shoppingBagRepository.findById(shoppingBagId)
                .orElseThrow(()-> new NotFoundException("ShoppingBag not found!"));
    }

    private void ifTrashBucketNotFoundThrowException(long userTrashId, long shoppingBagId){
        trashBucketRepository.findShoppingBagByUserTrashId( userTrashId, shoppingBagId)
                .orElseThrow(()-> new NotFoundException("TrashBucket not found!"));
    }

    private Set<ShoppingBagProduct> getShoppingBagProducts(ShoppingBag shoppingBag){
        return new HashSet<>(shoppingBagProductRepository.findAllByShoppingBag(shoppingBag));
    }

}
