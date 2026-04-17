package com.timetracking.repository;

import com.timetracking.domain.model.Attendance;
import java.util.*;

public interface IAttendanceRepository {
    void save(Attendance attendance);

    Attendance findById(int id);

    List<Attendance> findAll();

    void delete(int id);
}