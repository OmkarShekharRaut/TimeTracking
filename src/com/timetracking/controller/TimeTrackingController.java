package com.timetracking.controller;

import com.timetracking.service.*;
import com.timetracking.domain.model.*;

import java.util.*;

public class TimeTrackingController {

    private AttendanceService attendanceService;
    private BreakService breakService;
    private OvertimeService overtimeService;
    private ReportService reportService;

    public TimeTrackingController(
            AttendanceService attendanceService,
            BreakService breakService,
            OvertimeService overtimeService,
            ReportService reportService) {

        this.attendanceService = attendanceService;
        this.breakService = breakService;
        this.overtimeService = overtimeService;
        this.reportService = reportService;
    }

    public Attendance markPunchIn(int id) {
        try {
            return attendanceService.punchIn(id);
        } catch (com.timetracking.exception.TimeTrackingException e) {
            System.err.println("Error punching in: " + e.getMessage());
            return null;
        }
    }

    public void markPunchOut(int id) {
        try {
            attendanceService.punchOut(id);
        } catch (com.timetracking.exception.TimeTrackingException e) {
            System.err.println("Error punching out: " + e.getMessage());
        }
    }

    public BreakRecord startBreak(int breakId) {
        try {
            return breakService.startBreak(breakId);
        } catch (com.timetracking.exception.TimeTrackingException e) {
            System.err.println("Error starting break: " + e.getMessage());
            return null;
        }
    }

    public void endBreak(BreakRecord breakRecord) {
        try {
            breakService.endBreak(breakRecord);
        } catch (com.timetracking.exception.TimeTrackingException e) {
            System.err.println("Error ending break: " + e.getMessage());
        }
    }

    public Report generateReport(List<Attendance> attendanceList) {
        return reportService.generateReport(attendanceList);
    }

    public Overtime calculateOvertime(Attendance attendance, Policy policy) {
        try {
            return overtimeService.calculateOvertime(attendance, policy);
        } catch (Exception e) {
            System.err.println("Error calculating overtime: " + e.getMessage());
            return null;
        }
    }
}