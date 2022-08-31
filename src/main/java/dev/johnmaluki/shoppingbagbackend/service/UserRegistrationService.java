package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.User;

@FunctionalInterface
public interface UserRegistrationService {
    User registerUser(String username, String password);
}
