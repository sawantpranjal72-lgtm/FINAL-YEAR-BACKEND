package com.digitalfir.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fir_status_history")
public class FirStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FIR fir;

    @Enumerated(EnumType.STRING)
    private FirStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private FirStatus newStatus;

    private Long changedBy;  
    private LocalDateTime changedAt;

    @PrePersist
    protected void onChange() {
        this.changedAt = LocalDateTime.now();
    }


    public void setFir(FIR fir) {
        this.fir = fir;
    }

    public void setOldStatus(FirStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public void setNewStatus(FirStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }
}

