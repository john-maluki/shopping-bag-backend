package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBag;
import dev.johnmaluki.shoppingbagbackend.entity.ShoppingBagProduct;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.ShoppingBagProductRepository;
import dev.johnmaluki.shoppingbagbackend.repository.ShoppingBagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingBagProductServiceImpl implements ShoppingBagProductService {
    private final ShoppingBagProductRepository shoppingBagProductRepository;

    private final ShoppingBagRepository shoppingBagRepository;

    public ShoppingBagProductServiceImpl(ShoppingBagProductRepository shoppingBagProductRepository, ShoppingBagRepository shoppingBagRepository) {
        this.shoppingBagProductRepository = shoppingBagProductRepository;
        this.shoppingBagRepository = shoppingBagRepository;
    }

    @Override
    public List<ShoppingBagProduct> findAllShoppingBagProducts(long shoppingBagId) {
        ShoppingBag shoppingBag = getShoppingBagOrThrowExceptionIfDoesNotExist(shoppingBagId);
        return shoppingBagProductRepository.findAllByShoppingBag(shoppingBag);
    }

    @Override
    public ShoppingBagProduct updateShoppingBagProductQuantity(long shoppingBagProductId, int quantity) {
        ShoppingBagProduct shoppingBagProduct = getShoppingBagProductOrThrowExceptionIfDoesNotExist(shoppingBagProductId);
        shoppingBagProduct.setQuantity(quantity);
        return shoppingBagProductRepository.save(shoppingBagProduct);
    }

    private ShoppingBag getShoppingBagOrThrowExceptionIfDoesNotExist(long shoppingBagId){
        return shoppingBagRepository.findById(shoppingBagId)
                .orElseThrow(()-> new NotFoundException("ShoppingBag not found!"));
    }

    private ShoppingBagProduct getShoppingBagProductOrThrowExceptionIfDoesNotExist(long shoppingBagProductId){
        return shoppingBagProductRepository.findById(shoppingBagProductId)
                .orElseThrow(()-> new NotFoundException("ShoppingBagProduct not found!"));
    }
}
