package com.timetracking.service;

import com.timetracking.domain.model.*;

import java.time.*;

public class BreakService {

    public BreakRecord startBreak(int breakId) {
        return new BreakRecord(breakId, LocalDateTime.now());
    }

    public void endBreak(BreakRecord breakRecord) {
        breakRecord.endBreak(LocalDateTime.now());
    }
}