package com.timetracking.service;

import com.timetracking.domain.model.*;
import com.timetracking.exception.TimeTrackingException;
import com.timetracking.repository.*;
import com.timetracking.time.SystemTimeProvider;
import com.timetracking.time.TimeProvider;

import java.time.*;
import java.util.Objects;

public class AttendanceService {

    private IAttendanceRepository attendanceRepository;
    private final TimeProvider timeProvider;

    public AttendanceService(IAttendanceRepository repo) {
        this(repo, new SystemTimeProvider());
    }

    public AttendanceService(IAttendanceRepository repo, TimeProvider timeProvider) {
        this.attendanceRepository = repo;
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public Attendance punchIn(int id) {
        Attendance existing = attendanceRepository.findById(id);
        if (existing != null && existing.getPunchOutTime() == null) {
            throw new TimeTrackingException("Already punched in for id " + id + ".");
        }

        Attendance attendance = new Attendance(id, timeProvider.today(), timeProvider.now());

        attendanceRepository.save(attendance);

        return attendance;
    }

    public void punchOut(int id) {

        Attendance attendance = attendanceRepository.findById(id);

        if (attendance != null) {
            if (attendance.getPunchOutTime() != null) {
                throw new TimeTrackingException("Already punched out for id " + id + ".");
            }
            attendance.punchOut(timeProvider.now());
        } else {
            throw new com.timetracking.exception.AttendanceNotFoundException("Attendance record with id " + id + " not found.");
        }
    }
}