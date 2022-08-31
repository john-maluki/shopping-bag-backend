package dev.johnmaluki.shoppingbagbackend.exception;

public class ShoppingBagAlreadyTrashedException extends RuntimeException{
    public ShoppingBagAlreadyTrashedException(String message) {
        super(message);
    }
}
