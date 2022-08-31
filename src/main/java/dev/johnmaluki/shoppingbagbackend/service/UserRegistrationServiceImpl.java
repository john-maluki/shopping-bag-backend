package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.exception.UserRegistrationException;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService{
    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User registerUser(String username, String password) {
        this.checkUserWithUsernameExists(username);
        return this.registerUserWithUsernameAndPasswordOnly(username, password);
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    private User registerUserWithUsernameAndPasswordOnly(String username, String password){
        User user = User.builder()
                .username(username)
                .password(this.encodePassword(password))
                .build();

        return userRepository.save(user);
    }
    private void checkUserWithUsernameExists(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(this::deleteUserIfExistsAndUserAccountNotFullyRegistered);
    }

    private void deleteUserIfExistsAndUserAccountNotFullyRegistered(User user){
        boolean canDeleteAccount = user.getIsActive() == 0 && user.getFirstName() == null && user.getLastName() == null
                && user.getMiddleName() == null && user.getEmail() == null;
        if(canDeleteAccount) {
            userRepository.delete(user);
        } else {
            throw new UserRegistrationException("User found!");
        }
    }
}
