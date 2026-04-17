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
        this.breakEndTime = endTime;
        Duration duration = Duration.between(breakStartTime, breakEndTime);
        breakDuration = duration.toMinutes() / 60.0;
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