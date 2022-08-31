package dev.johnmaluki.shoppingbagbackend.security;

import dev.johnmaluki.shoppingbagbackend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //Add user roles
        this.user.getRolesAndPermsList()
                .forEach(val -> grantedAuthorities
                        .add(new SimpleGrantedAuthority(val)
        ));


        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getExpired() == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive() == 1;
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String firstName() {
        return user.getFirstName();
    }

    public long getId() {
        return user.getId();
    }


    public String lastName() {
        return user.getLastName();
    }
}
