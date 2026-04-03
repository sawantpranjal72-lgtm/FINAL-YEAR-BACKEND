package com.digitalfir.backend.util;

import com.digitalfir.backend.model.FirStatus;
import java.util.List;
import java.util.Map;

public class FirStatusFlow {

    private static final Map<FirStatus, List<FirStatus>> FLOW = Map.of(
        FirStatus.CREATED, List.of(FirStatus.SUBMITTED),
        FirStatus.SUBMITTED, List.of(FirStatus.UNDER_REVIEW),
        FirStatus.UNDER_REVIEW, List.of(FirStatus.APPROVED, FirStatus.REJECTED),
        FirStatus.APPROVED, List.of(FirStatus.CLOSED),
        FirStatus.REJECTED, List.of(FirStatus.CLOSED)
    );

    public static boolean isValidTransition(FirStatus current, FirStatus next) {
        return FLOW.getOrDefault(current, List.of()).contains(next);
    }
}
