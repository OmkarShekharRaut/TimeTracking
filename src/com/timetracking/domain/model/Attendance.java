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
        breaks.add(breakRecord);
    }

    public void punchOut(LocalDateTime outTime) {
        this.punchOutTime = outTime;

        Duration duration = Duration.between(punchInTime, punchOutTime);
        totalWorkHours = duration.toMinutes() / 60.0;

        for (BreakRecord br : breaks) {
            totalWorkHours -= br.getBreakDuration();
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