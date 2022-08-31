package dev.johnmaluki.shoppingbagbackend.service;

import dev.johnmaluki.shoppingbagbackend.entity.Permission;
import dev.johnmaluki.shoppingbagbackend.entity.Role;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.entity.UserTrash;
import dev.johnmaluki.shoppingbagbackend.exception.NotFoundException;
import dev.johnmaluki.shoppingbagbackend.repository.PermissionRepository;
import dev.johnmaluki.shoppingbagbackend.repository.RoleRepository;
import dev.johnmaluki.shoppingbagbackend.repository.UserRepository;
import dev.johnmaluki.shoppingbagbackend.repository.UserTrashRepository;
import dev.johnmaluki.shoppingbagbackend.security.UserPrincipal;
import dev.johnmaluki.shoppingbagbackend.security.user.UserRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTrashRepository userTrashRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional
    public User updateUser(User user) {
        User updateUser = userRepository.findById(user.getId()).get();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setMiddleName(user.getMiddleName());
        updateUser.setLastName(user.getLastName());
        updateUser.setEmail(user.getEmail());
        updateUser.setMobileNumber(user.getMobileNumber());
        updateUser.setUsername(user.getUsername());
        updateUser.setIsActive(1);
        updateUser = activateAccount(updateUser); // activate if not activated
        User savedUser = userRepository.save(updateUser);

        createUserTrash(savedUser); // Create user trash if it does not have one
        createUserDefaultRole(savedUser); // Create user roles if it is new user
        return savedUser;
    }

    private void createUserTrash(User user){
        Optional<UserTrash> userTrashOptional = userTrashRepository.findUserTrashByUserId(user.getId());
        if (userTrashOptional.isEmpty()){
            UserTrash userTrash = UserTrash.builder()
                    .user(user)
                    .build();
            userTrashRepository.save(userTrash);
        }
    }

    private void createUserDefaultRole(User user){
        Set<Role> roles = new HashSet<>(roleRepository.findUserRoles(user));
        boolean exists = roles.stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase(UserRoles.USER.toLowerCase()));
        if(!exists) {
            Role role = Role.builder()
                    .user(user)
                    .permissions(this.createUserDefaultPerms())
                    .roleName(UserRoles.USER)
                    .build();
            roleRepository.save(role);
        }
    }

    private Set<Permission> createUserDefaultPerms(){
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAll());
        Set<String> permString = UserRoles.getUserRoles();
        Set<Permission> finalPerms = permissions.stream()
                .filter(perm -> permString.contains(perm.getPermissionName())).collect(Collectors.toSet());
        return finalPerms;
    }

    private User activateAccount(User user){
        user.setIsActive(1);
        return user;
    }

    private UserPrincipal getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        return new UserPrincipal(optionalUser.get());
    }
}
