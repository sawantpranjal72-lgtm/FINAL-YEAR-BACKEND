package com.digitalfir.repository;

import com.digitalfir.backend.model.FirStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirStatusHistoryRepository
        extends JpaRepository<FirStatusHistory, Long> {

    List<FirStatusHistory> findByFirId(Long firId);
}

