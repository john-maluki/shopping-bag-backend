package dev.johnmaluki.shoppingbagbackend.exception;

public class UserRegistrationException extends RuntimeException{
    public UserRegistrationException(String message) {
        super(message);
    }
}
