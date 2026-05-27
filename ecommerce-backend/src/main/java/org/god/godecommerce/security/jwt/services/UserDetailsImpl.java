package org.god.godecommerce.security.jwt.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.god.godecommerce.model.User;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDetailsImpl(
        Long id,
        String userName,
        String email,
        @JsonIgnore String password,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    public static UserDetailsImpl build(User user) {
        Set<GrantedAuthority> authorityList = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRoleName().name()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                authorityList
        );
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    @NonNull
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
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
        return true;
    }
}