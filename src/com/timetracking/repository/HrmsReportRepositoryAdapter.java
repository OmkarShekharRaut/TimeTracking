package com.timetracking.repository;

import com.timetracking.domain.model.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsReportRepositoryAdapter implements IReportRepository {

    private final com.hrms.db.repositories.timetracking.IReportRepository delegate;
    private final Map<Integer, Report> sessionCache = new ConcurrentHashMap<>();

    public HrmsReportRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingReportRepositoryImpl();
    }

    @Override
    public void save(Report report) {
        if (report == null) {
            return;
        }
        sessionCache.put(report.getReportId(), report);
        delegate.save(toHrmsReport(report));
    }

    @Override
    public Report findById(int id) {
        Report cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }
        com.hrms.db.entities.HrReport stored = delegate.findById(String.valueOf(id));
        Report mapped = toDomainReport(stored);
        if (mapped != null) {
            sessionCache.put(mapped.getReportId(), mapped);
        }
        return mapped;
    }

    @Override
    public List<Report> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete(String.valueOf(id));
    }

    private com.hrms.db.entities.HrReport toHrmsReport(Report report) {
        com.hrms.db.entities.HrReport mapped = new com.hrms.db.entities.HrReport();
        mapped.setReportId(String.valueOf(report.getReportId()));
        mapped.setReportName("TimeTracking Report " + report.getReportId());
        mapped.setReportType("TimeTracking");
        mapped.setGeneratedDate(report.getEndDate() != null ? report.getEndDate() : LocalDate.now());
        mapped.setExportFormat("N/A");
        return mapped;
    }

    private Report toDomainReport(com.hrms.db.entities.HrReport hrReport) {
        if (hrReport == null || hrReport.getReportId() == null) {
            return null;
        }
        int reportId;
        try {
            reportId = Integer.parseInt(hrReport.getReportId());
        } catch (NumberFormatException ex) {
            return null;
        }
        LocalDate generatedDate = hrReport.getGeneratedDate() != null ? hrReport.getGeneratedDate() : LocalDate.now();
        return new Report(reportId, generatedDate, generatedDate, 0.0);
    }
}
