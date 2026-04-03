package com.digitalfir.repository;

import com.digitalfir.backend.model.Evidence;
import com.digitalfir.backend.model.FIR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

 
    List<Evidence> findByFirAndIsDeletedFalse(FIR fir);
}

