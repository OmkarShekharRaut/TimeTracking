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
        return attendanceService.punchIn(id);
    }

    public void markPunchOut(int id) {
        attendanceService.punchOut(id);
    }

    public BreakRecord startBreak(int breakId) {
        return breakService.startBreak(breakId);
    }

    public void endBreak(BreakRecord breakRecord) {
        breakService.endBreak(breakRecord);
    }

    public Report generateReport(List<Attendance> attendanceList) {
        return reportService.generateReport(attendanceList);
    }
}