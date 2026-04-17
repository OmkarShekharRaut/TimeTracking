package com.timetracking.service;

import com.timetracking.domain.model.*;

import java.time.*;
import java.util.*;

public class ReportService {

    public Report generateReport(List<Attendance> attendanceList) {

        double totalHours = 0;

        for (Attendance a : attendanceList) {
            totalHours += a.getTotalWorkHours();
        }

        return new Report(
                1,
                LocalDate.now().minusDays(30),
                LocalDate.now(),
                totalHours);
    }

    public void printReport(Report report) {

        System.out.println("TOTAL HOURS: " + report.getTotalHours());
    }
}