package com.mourad.backend.infrastructure.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Hard-coded user store for V1.
 * Replace with a database-backed implementation when user management is added.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetails adminUser;

    public UserDetailsServiceImpl(@Lazy PasswordEncoder passwordEncoder) {
        this.adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return adminUser;
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
