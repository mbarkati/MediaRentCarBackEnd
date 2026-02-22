package com.mourad.backend.infrastructure.security;

import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.domain.port.out.AdminAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminAccountRepository adminAccountRepository;

    public UserDetailsServiceImpl(AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminAccount admin = adminAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getHashedPassword())
                .roles("ADMIN")
                .build();
    }
}
