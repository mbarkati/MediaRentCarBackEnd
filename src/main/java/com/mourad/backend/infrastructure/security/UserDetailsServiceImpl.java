package com.mourad.backend.infrastructure.security;

import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.domain.port.out.AdminAccountRepository;
import com.mourad.backend.domain.port.out.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminAccountRepository adminAccountRepository;
    private final AppUserRepository appUserRepository;

    public UserDetailsServiceImpl(AdminAccountRepository adminAccountRepository,
                                  AppUserRepository appUserRepository) {
        this.adminAccountRepository = adminAccountRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Admin accounts (subject = username)
        Optional<AdminAccount> admin = adminAccountRepository.findByUsername(username);
        if (admin.isPresent()) {
            return User.builder()
                    .username(admin.get().getUsername())
                    .password(admin.get().getHashedPassword())
                    .roles("ADMIN")
                    .build();
        }

        // 2. Mobile app users (subject = phone number)
        return appUserRepository.findByPhone(username)
                .map(user -> User.builder()
                        .username(user.getPhone())
                        .password(user.getPasswordHash())
                        .roles("APP_USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
