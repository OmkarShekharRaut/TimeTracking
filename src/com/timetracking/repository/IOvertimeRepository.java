package com.timetracking.repository;

import com.timetracking.domain.model.Overtime;
import java.util.List;

public interface IOvertimeRepository {
    void save(Overtime overtime);

    Overtime findById(int id);

    List<Overtime> findAll();

    void delete(int id);
}
