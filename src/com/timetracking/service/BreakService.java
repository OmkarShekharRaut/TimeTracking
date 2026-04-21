package com.timetracking.service;

import com.timetracking.domain.model.*;

import java.time.*;

public class BreakService {

    public BreakRecord startBreak(int breakId) {
        return new BreakRecord(breakId, LocalDateTime.now());
    }

    public void endBreak(BreakRecord breakRecord) {
        if (breakRecord == null) {
            throw new com.timetracking.exception.InvalidBreakException("Invalid break record provided.");
        }
        breakRecord.endBreak(LocalDateTime.now());
    }
}