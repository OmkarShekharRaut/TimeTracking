package com.timetracking.service;

import com.timetracking.domain.model.*;
import com.timetracking.repository.*;

import java.time.*;

public class AttendanceService {

    private IAttendanceRepository attendanceRepository;

    public AttendanceService(IAttendanceRepository repo) {
        this.attendanceRepository = repo;
    }

    public Attendance punchIn(int id) {

        Attendance attendance = new Attendance(id, LocalDate.now(), LocalDateTime.now());

        attendanceRepository.save(attendance);

        return attendance;
    }

    public void punchOut(int id) {

        Attendance attendance = attendanceRepository.findById(id);

        if (attendance != null) {
            attendance.punchOut(LocalDateTime.now());
        } else {
            throw new com.timetracking.exception.AttendanceNotFoundException("Attendance record with id " + id + " not found.");
        }
    }
}