package com.timetracking.repository;

import com.timetracking.domain.model.Report;
import java.util.List;

public interface IReportRepository {
    void save(Report report);

    Report findById(int id);

    List<Report> findAll();

    void delete(int id);
}
