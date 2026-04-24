package com.timetracking.domain.model;

import java.time.*;

public class BreakRecord {
    private int breakId;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;
    private double breakDuration;

    public BreakRecord(int breakId, LocalDateTime breakStartTime) {
        this.breakId = breakId;
        this.breakStartTime = breakStartTime;
    }

    public void endBreak(LocalDateTime endTime) {
        if (breakStartTime == null) {
            throw new com.timetracking.exception.InvalidBreakException("Break start time is not set.");
        }
        if (breakEndTime != null) {
            throw new com.timetracking.exception.InvalidBreakException("Break already ended.");
        }
        if (endTime == null) {
            throw new com.timetracking.exception.InvalidBreakException("Break end time cannot be null.");
        }
        if (endTime.isBefore(breakStartTime)) {
            throw new com.timetracking.exception.InvalidBreakException("Break end time cannot be before start time.");
        }
        this.breakEndTime = endTime;
        Duration duration = Duration.between(breakStartTime, breakEndTime);
        breakDuration = duration.toMinutes() / 60.0;
    }

    public boolean isEnded() {
        return breakEndTime != null;
    }

    public int getBreakId() {
        return breakId;
    }

    public LocalDateTime getBreakStartTime() {
        return breakStartTime;
    }

    public LocalDateTime getBreakEndTime() {
        return breakEndTime;
    }

    public double getBreakDuration() {
        return breakDuration;
    }
}