package com.timetracking.service;

import com.timetracking.domain.model.*;
import com.timetracking.time.SystemTimeProvider;
import com.timetracking.time.TimeProvider;

import java.util.*;
import java.util.Objects;

public class ReportService {

    private final TimeProvider timeProvider;

    public ReportService() {
        this(new SystemTimeProvider());
    }

    public ReportService(TimeProvider timeProvider) {
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public Report generateReport(List<Attendance> attendanceList) {

        double totalHours = 0;

        if (attendanceList != null) {
            for (Attendance a : attendanceList) {
                if (a != null) {
                    totalHours += a.getTotalWorkHours();
                }
            }
        }

        return new Report(
                1,
                timeProvider.today().minusDays(30),
                timeProvider.today(),
                totalHours);
    }

    public void printReport(Report report) {

        System.out.println("TOTAL HOURS: " + report.getTotalHours());
    }
}