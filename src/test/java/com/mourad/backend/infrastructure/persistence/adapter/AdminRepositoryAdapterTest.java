package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.AdminAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Persistence integration test: Flyway migrations V1-V3 + JPA mapping + AdminRepositoryAdapter.
 * Uses H2 in-memory (profile "test"). Each test runs in a transaction rolled back afterwards.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class AdminRepositoryAdapterTest {

    @Autowired
    private AdminRepositoryAdapter sut;

    // ── save + findByUsername ─────────────────────────────────────────────────

    @Test
    void save_andFindByUsername_shouldRoundTripAllFields() {
        AdminAccount account = AdminAccount.create("alice", "$2a$10$hashedpassword");

        sut.save(account);

        Optional<AdminAccount> found = sut.findByUsername("alice");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("alice");
        assertThat(found.get().getHashedPassword()).isEqualTo("$2a$10$hashedpassword");
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getCreatedAt()).isNotNull();
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUserDoesNotExist() {
        Optional<AdminAccount> found = sut.findByUsername("nobody");

        assertThat(found).isEmpty();
    }

    // ── existsByUsername ──────────────────────────────────────────────────────

    @Test
    void existsByUsername_shouldReturnTrue_whenUserExists() {
        sut.save(AdminAccount.create("bob", "hashed"));

        assertThat(sut.existsByUsername("bob")).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUserDoesNotExist() {
        assertThat(sut.existsByUsername("ghost")).isFalse();
    }
}
