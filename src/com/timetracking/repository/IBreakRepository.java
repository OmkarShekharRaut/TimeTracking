package com.timetracking.repository;

import com.timetracking.domain.model.BreakRecord;
import java.util.List;

public interface IBreakRepository {
    void save(BreakRecord breakRecord);

    BreakRecord findById(int id);

    List<BreakRecord> findAll();

    void delete(int id);
}
