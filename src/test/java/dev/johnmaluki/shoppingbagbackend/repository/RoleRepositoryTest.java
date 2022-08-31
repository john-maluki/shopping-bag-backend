package dev.johnmaluki.shoppingbagbackend.repository;

import dev.johnmaluki.shoppingbagbackend.entity.Permission;
import dev.johnmaluki.shoppingbagbackend.entity.Role;
import dev.johnmaluki.shoppingbagbackend.entity.User;
import dev.johnmaluki.shoppingbagbackend.security.user.UserRoles;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("johnDoe@test.com")
                .firstName("John")
                .middleName("Net")
                .lastName("Doe")
                .mobileNumber("0700000000")
                .build();
        userRepository.save(user);

        User user1 = User.builder()
                .email("johnDoe1@test.com")
                .firstName("John1")
                .middleName("Net1")
                .lastName("Doe1")
                .mobileNumber("0700000001")
                .build();
        userRepository.save(user1);
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("[Repository] Test saving user roles record")
    public void saveUserRoleTest(){
        User savedUser = userRepository.findById(1l).get();
        Set<Permission> permissions = stringSetToPermsSet(UserRoles.getUserRoles());
        Role role = Role.builder()
                .user(savedUser)
                .roleName(UserRoles.USER)
                .build();
        role.setPermissions(permissions);
        roleRepository.save(role);

        List<Role> userRoles = roleRepository.findUserRoles(savedUser);

        assertThat(userRoles).hasSize(1);
        assertThat(userRoles.get(0).getUser()).isEqualTo(savedUser);
        assertThat(userRoles.get(0).getPermissions()).hasSize(permissions.size());
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    @DisplayName("[Repository] Test finding user role records")
    void findUserRolesTest() {
        User savedUser = userRepository.findById(1l).get();

        List<Role> userRoles = roleRepository.findUserRoles(savedUser);

        assertThat(userRoles).hasSize(1);
        assertThat(userRoles.get(0).getPermissions()).hasSize(UserRoles.getUserRoles().size());
        assertThat(userRoles.get(0).getUser()).isEqualTo(savedUser);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("[Repository] Test adding shopkeeper roles to existing user role records")
    void addShopkeeperRolesToExistingUserTest() {
        User savedUser = userRepository.findById(1l).get();
        Set<Permission> permissions = stringSetToPermsSet(UserRoles.getShopkeeperRoles());
        Role role = Role.builder()
                .user(savedUser)
                .roleName(UserRoles.SHOPKEEPER)
                .build();
        role.setPermissions(permissions);
        roleRepository.save(role);

        List<Role> userRoles = roleRepository.findUserRoles(savedUser);

        assertThat(userRoles).hasSize(2);
        assertThat(userRoles.get(0).getUser()).isEqualTo(savedUser);
        assertThat(userRoles.get(1).getUser()).isEqualTo(savedUser);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("[Repository] Test fetching user should also contain user role records")
    void gettingUserShouldAllFetchRolesTest() {
        User savedUser = userRepository.findById(1l).get();

        List<Role> userRoles = savedUser.getRoles();

        assertThat(userRoles.stream().distinct()).hasSize(2);
    }

    @Test
    @Disabled
    @Order(5)
    @Rollback(value = false)
    @DisplayName("[Repository] Test deleting user should delete all user role records")
    void deleteUserShouldDeleteAllRolesTest() {
        User savedUser1 = userRepository.findById(1l).get();
        User savedUser2 = userRepository.findById(2l).get();
        Set<Permission> user2Permissions = stringSetToPermsSet(UserRoles.getUserRoles());
        Role role = Role.builder()
                .user(savedUser2)
                .roleName(UserRoles.USER)
                .build();
        role.setPermissions(user2Permissions);
        roleRepository.save(role);

        userRepository.delete(savedUser1);
        List<Role> user1Roles = roleRepository.findUserRoles(savedUser1);
        List<Role> user2Roles = roleRepository.findUserRoles(savedUser2);
        List<Permission> permissions = permissionRepository.findAll();

        assertThat(user1Roles).hasSize(0);
        assertThat(user2Roles).hasSize(1);
        assertThat(user2Roles.get(0).getUser()).isEqualTo(savedUser2);
        assertThat(permissions).hasSize(user2Permissions.size());

    }

    private Set<Permission> stringSetToPermsSet(Set<String> stringSet){
        return stringSet
                .stream()
                .map(perm -> Permission.builder().permissionName(perm).build())
                .collect(Collectors.toSet());
    }
}