package com.timetracking.repository;

import com.timetracking.domain.model.Attendance;
import java.util.*;

public class MockAttendanceRepository implements IAttendanceRepository {

    private List<Attendance> attendanceList = new ArrayList<>();

    @Override
    public void save(Attendance attendance) {
        attendanceList.add(attendance);
    }

    @Override
    public Attendance findById(int id) {
        for (Attendance a : attendanceList) {
            if (a.getAttendanceId() == id)
                return a;
        }
        return null;
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceList;
    }

    @Override
    public void delete(int id) {
        attendanceList.removeIf(a -> a.getAttendanceId() == id);
    }
}