package com.timetracking.repository;

import com.timetracking.domain.model.Attendance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsAttendanceRepositoryAdapter implements IAttendanceRepository {

    private final com.hrms.db.repositories.timetracking.IAttendanceRepository delegate;
    private final com.hrms.db.repositories.timetracking.IEmployeeRepository employeeDelegate;
    private final Map<Integer, Attendance> sessionCache = new ConcurrentHashMap<>();

    public HrmsAttendanceRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingAttendanceRepositoryImpl();
        this.employeeDelegate = new com.hrms.db.repositories.timetracking.TimeTrackingEmployeeRepositoryImpl();
    }

    @Override
    public void save(Attendance attendance) {
        if (attendance == null) {
            return;
        }

        try {
            delegate.save(toHrmsAttendance(attendance));
        } catch (RuntimeException ex) {
            persistViaSqlite(attendance);
        }
        sessionCache.put(attendance.getAttendanceId(), attendance);
    }

    @Override
    public Attendance findById(int id) {
        Attendance inMemory = sessionCache.get(id);
        if (inMemory != null) {
            return inMemory;
        }

        com.hrms.db.entities.Attendance stored = delegate.findById(String.valueOf(id));
        if (stored == null) {
            return null;
        }

        // The DB entity does not expose punch-in/punch-out details used by this subsystem.
        // We create a minimal domain object and cache it for the current session.
        Attendance fallback = new Attendance(id, java.time.LocalDate.now(), java.time.LocalDateTime.now());
        sessionCache.put(id, fallback);
        return fallback;
    }

    @Override
    public List<Attendance> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete(String.valueOf(id));
    }

    private com.hrms.db.entities.Attendance toHrmsAttendance(Attendance attendance) {
        com.hrms.db.entities.Attendance hrmsAttendance = new com.hrms.db.entities.Attendance();
        hrmsAttendance.setRecordId(String.valueOf(attendance.getAttendanceId()));
        hrmsAttendance.setEmployee(resolveOrCreateEmployee(attendance.getAttendanceId()));
        hrmsAttendance.setPayPeriod(attendance.getDate() != null
                ? attendance.getDate().toString().substring(0, 7)
                : java.time.LocalDate.now().toString().substring(0, 7));
        hrmsAttendance.setWorkingDaysInMonth(0);
        hrmsAttendance.setLeaveWithPay(0);
        hrmsAttendance.setLeaveWithoutPay(0);
        hrmsAttendance.setHoursWorked(attendance.getTotalWorkHours());
        hrmsAttendance.setOvertimeHours(0.0);
        return hrmsAttendance;
    }

    private com.hrms.db.entities.Employee resolveOrCreateEmployee(int attendanceId) {
        String employeeId = String.valueOf(attendanceId);
        com.hrms.db.entities.Employee employee = employeeDelegate.findById(employeeId);
        if (employee != null) {
            return employee;
        }

        com.hrms.db.entities.Employee created = new com.hrms.db.entities.Employee();
        created.setEmpId(employeeId);
        created.setName("Employee " + employeeId);
        created.setDepartment("General");
        created.setRole("Staff");
        employeeDelegate.save(created);

        com.hrms.db.entities.Employee persisted = employeeDelegate.findById(employeeId);
        if (persisted != null) {
            return persisted;
        }
        return created;
    }

    private void persistViaSqlite(Attendance attendance) {
        String employeeId = String.valueOf(attendance.getAttendanceId());
        String payPeriod = attendance.getDate() != null
                ? attendance.getDate().toString().substring(0, 7)
                : java.time.LocalDate.now().toString().substring(0, 7);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:hrms.db")) {
            try (PreparedStatement employeeStmt = connection.prepareStatement(
                    "INSERT OR IGNORE INTO employees (emp_id, name) VALUES (?, ?)")) {
                employeeStmt.setString(1, employeeId);
                employeeStmt.setString(2, "Employee " + employeeId);
                employeeStmt.executeUpdate();
            }

            try (PreparedStatement attendanceStmt = connection.prepareStatement(
                    "INSERT OR REPLACE INTO attendance " +
                            "(record_id, pay_period, emp_id, hours_worked, working_days_in_month, leave_with_pay, leave_without_pay, overtime_hours) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                attendanceStmt.setString(1, employeeId);
                attendanceStmt.setString(2, payPeriod);
                attendanceStmt.setString(3, employeeId);
                attendanceStmt.setDouble(4, attendance.getTotalWorkHours());
                attendanceStmt.setInt(5, 0);
                attendanceStmt.setInt(6, 0);
                attendanceStmt.setInt(7, 0);
                attendanceStmt.setDouble(8, 0.0);
                attendanceStmt.executeUpdate();
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to persist attendance to SQLite fallback.", sqlException);
        }
    }
}
