package com.timetracking.repository;

import com.timetracking.domain.model.Attendance;
import java.util.*;

public class MockAttendanceRepository implements IAttendanceRepository {

    private final Map<Integer, Attendance> attendanceById = new HashMap<>();

    @Override
    public void save(Attendance attendance) {
        if (attendance == null) {
            throw new IllegalArgumentException("attendance cannot be null");
        }
        attendanceById.put(attendance.getAttendanceId(), attendance);
    }

    @Override
    public Attendance findById(int id) {
        return attendanceById.get(id);
    }

    @Override
    public List<Attendance> findAll() {
        return new ArrayList<>(attendanceById.values());
    }

    @Override
    public void delete(int id) {
        attendanceById.remove(id);
    }
}