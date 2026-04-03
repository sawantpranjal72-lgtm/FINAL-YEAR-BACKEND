package com.digitalfir.repository;

import com.digitalfir.backend.model.PoliceProfile;
import com.digitalfir.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoliceProfileRepository
        extends JpaRepository<PoliceProfile, Long> {

    Optional<PoliceProfile> findByUser(User user);
  

        Optional<PoliceProfile> findByUserEmail(String email);
    }


