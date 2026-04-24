package com.timetracking.service;

import com.timetracking.domain.model.*;
import com.timetracking.time.SystemTimeProvider;
import com.timetracking.time.TimeProvider;

import java.time.*;
import java.util.Objects;

public class BreakService {

    private final TimeProvider timeProvider;

    public BreakService() {
        this(new SystemTimeProvider());
    }

    public BreakService(TimeProvider timeProvider) {
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public BreakRecord startBreak(int breakId) {
        return new BreakRecord(breakId, timeProvider.now());
    }

    public void endBreak(BreakRecord breakRecord) {
        if (breakRecord == null) {
            throw new com.timetracking.exception.InvalidBreakException("Invalid break record provided.");
        }
        breakRecord.endBreak(timeProvider.now());
    }
}