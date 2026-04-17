package com.timetracking.domain.model;

import java.time.*;

public class Report {
    private int reportId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalHours;

    public Report(int reportId, LocalDate startDate,
            LocalDate endDate, double totalHours) {
        this.reportId = reportId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalHours = totalHours;
    }

    public int getReportId() {
        return reportId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalHours() {
        return totalHours;
    }
}