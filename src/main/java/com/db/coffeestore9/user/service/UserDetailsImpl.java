package com.db.coffeestore9.user.service;

import com.db.coffeestore9.security.domain.Authority;
import com.db.coffeestore9.user.domain.User;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class UserDetailsImpl implements UserDetails {

    private final transient User user;
    private final transient Set<Authority> authorities;

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("UserDetailsImpl -> getAuthorities : OK");

        if (authorities.isEmpty()) {
            log.info("authorities is empty");
        } else {
            log.info("authorities size: {}", authorities.size());
        }
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole().name()))
                .collect(Collectors.toSet());
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
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
