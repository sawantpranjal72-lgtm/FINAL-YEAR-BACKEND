package com.digitalfir.repository;

import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    long countByEnabledFalse();

    Optional<User> findByResetToken(String resetToken);
}



