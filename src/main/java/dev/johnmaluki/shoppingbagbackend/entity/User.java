package dev.johnmaluki.shoppingbagbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.johnmaluki.shoppingbagbackend.util.entity.AccountStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"roles", "userTrash"})
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_address")
    private String email; // if not present defaults to username

    @Column(name = "username", unique = true)
    private String username; // email allowed

    @Column(name = "password")
    private String password; // used as username

    @Builder.Default
    @Column(name = "active")
    private int isActive = AccountStatus.INACTIVE;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Role> roles = new ArrayList<>();

    @Builder.Default
    @Column(name = "expired")
    private int expired = AccountStatus.UNEXPIRED;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserTrash userTrash;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ShopKeeper shopKeeper;

    public Set<String> getRolesAndPermsList(){
        // TODO - check why values of property `this.roles` are duplicated.
        Set<String> rolesAndPerms = new HashSet<>();
        for (Role role: this.getRoles()) {
            rolesAndPerms.add("ROLE_" + role.getRoleName());
            rolesAndPerms.addAll(
                    role.getPermissions().stream()
                            .map(Permission::getPermissionName)
                            .toList()
            );
        }
        return rolesAndPerms;
    }
}
