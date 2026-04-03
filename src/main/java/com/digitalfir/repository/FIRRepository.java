package com.digitalfir.repository;

import com.digitalfir.backend.model.FIR;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FIRRepository extends JpaRepository<FIR, Long> {
    List<FIR> findByCreatedBy(Long userId);
    Optional<FIR> findByFirNumber(String firNumber);
}

