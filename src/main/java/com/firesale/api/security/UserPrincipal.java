package com.firesale.api.security;

import com.firesale.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Should ideally be implemented
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsLocked(); // Should ideally be implemented
    }
}