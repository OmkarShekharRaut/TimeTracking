package com.timetracking.controller;

import com.timetracking.service.*;
import com.timetracking.domain.model.*;
import com.timetracking.reporting.ExceptionReportEntry;
import com.timetracking.reporting.ExceptionReporter;

import java.util.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

public class TimeTrackingController {

    private AttendanceService attendanceService;
    private BreakService breakService;
    private OvertimeService overtimeService;
    private ReportService reportService;
    private ExceptionReporter exceptionReporter;

    public TimeTrackingController(
            AttendanceService attendanceService,
            BreakService breakService,
            OvertimeService overtimeService,
            ReportService reportService) {

        this.attendanceService = attendanceService;
        this.breakService = breakService;
        this.overtimeService = overtimeService;
        this.reportService = reportService;
        this.exceptionReporter = null;
    }

    public TimeTrackingController(
            AttendanceService attendanceService,
            BreakService breakService,
            OvertimeService overtimeService,
            ReportService reportService,
            ExceptionReporter exceptionReporter) {
        this.attendanceService = attendanceService;
        this.breakService = breakService;
        this.overtimeService = overtimeService;
        this.reportService = reportService;
        this.exceptionReporter = exceptionReporter;
    }

    private void reportException(String operation, Exception e) {
        if (exceptionReporter == null || e == null) {
            return;
        }
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        exceptionReporter.report(new ExceptionReportEntry(
                LocalDateTime.now(),
                operation,
                e.getClass().getName(),
                e.getMessage(),
                sw.toString()
        ));
    }

    public Attendance markPunchIn(int id) {
        try {
            return attendanceService.punchIn(id);
        } catch (Exception e) {
            System.err.println("Error punching in: " + e.getMessage());
            reportException("markPunchIn(" + id + ")", e);
            return null;
        }
    }

    public void markPunchOut(int id) {
        try {
            attendanceService.punchOut(id);
        } catch (Exception e) {
            System.err.println("Error punching out: " + e.getMessage());
            reportException("markPunchOut(" + id + ")", e);
        }
    }

    public BreakRecord startBreak(int breakId) {
        try {
            return breakService.startBreak(breakId);
        } catch (Exception e) {
            System.err.println("Error starting break: " + e.getMessage());
            reportException("startBreak(" + breakId + ")", e);
            return null;
        }
    }

    public void endBreak(BreakRecord breakRecord) {
        try {
            breakService.endBreak(breakRecord);
        } catch (Exception e) {
            System.err.println("Error ending break: " + e.getMessage());
            reportException("endBreak(breakRecord)", e);
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
            reportException("calculateOvertime(attendance, policy)", e);
            return null;
        }
    }
}