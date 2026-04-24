package com.timetracking.domain.model;

import java.time.*;
import java.util.*;

public class Attendance {

    private int attendanceId;
    private LocalDate date;
    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;
    private double totalWorkHours;

    private List<BreakRecord> breaks = new ArrayList<>();

    public Attendance(int attendanceId, LocalDate date, LocalDateTime punchInTime) {
        this.attendanceId = attendanceId;
        this.date = date;
        this.punchInTime = punchInTime;
    }

    public void addBreak(BreakRecord breakRecord) {
        if (breakRecord == null) {
            throw new com.timetracking.exception.InvalidBreakException("Invalid break record provided.");
        }
        if (!breakRecord.isEnded()) {
            throw new com.timetracking.exception.InvalidBreakException("Cannot add a break that hasn't ended.");
        }
        if (breakRecord.getBreakStartTime() != null && punchInTime != null
                && breakRecord.getBreakStartTime().isBefore(punchInTime)) {
            throw new com.timetracking.exception.InvalidBreakException("Break cannot start before punch-in.");
        }
        if (punchOutTime != null && breakRecord.getBreakEndTime() != null
                && breakRecord.getBreakEndTime().isAfter(punchOutTime)) {
            throw new com.timetracking.exception.InvalidBreakException("Break cannot end after punch-out.");
        }
        breaks.add(breakRecord);
    }

    public void punchOut(LocalDateTime outTime) {
        if (punchInTime == null) {
            throw new com.timetracking.exception.TimeTrackingException("Cannot punch out without punch-in time.");
        }
        if (punchOutTime != null) {
            throw new com.timetracking.exception.TimeTrackingException("Already punched out.");
        }
        if (outTime == null) {
            throw new com.timetracking.exception.TimeTrackingException("Punch out time cannot be null.");
        }
        if (outTime.isBefore(punchInTime)) {
            throw new com.timetracking.exception.TimeTrackingException("Punch out time cannot be before punch-in time.");
        }
        this.punchOutTime = outTime;

        Duration duration = Duration.between(punchInTime, punchOutTime);
        totalWorkHours = duration.toMinutes() / 60.0;

        for (BreakRecord br : breaks) {
            totalWorkHours -= br.getBreakDuration();
        }

        if (totalWorkHours < 0) {
            totalWorkHours = 0;
        }
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getPunchInTime() {
        return punchInTime;
    }

    public LocalDateTime getPunchOutTime() {
        return punchOutTime;
    }

    public double getTotalWorkHours() {
        return totalWorkHours;
    }

    public List<BreakRecord> getBreaks() {
        return breaks;
    }
}