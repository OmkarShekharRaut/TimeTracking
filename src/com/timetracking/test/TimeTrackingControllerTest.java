package com.timetracking.test;

import com.timetracking.controller.TimeTrackingController;
import com.timetracking.domain.model.Attendance;
import com.timetracking.domain.model.BreakRecord;
import com.timetracking.domain.model.Report;
import com.timetracking.repository.IAttendanceRepository;
import com.timetracking.service.*;
import java.util.ArrayList;
import java.util.List;

public class TimeTrackingControllerTest {

    private TimeTrackingController controller;
    private TestAttendanceRepository testRepo;

    public void setUp() {
        testRepo = new TestAttendanceRepository();
        com.timetracking.time.TimeProvider timeProvider = new com.timetracking.time.SystemTimeProvider();
        AttendanceService attendanceService = new AttendanceService(testRepo, timeProvider);
        BreakService breakService = new BreakService(timeProvider);
        OvertimeService overtimeService = new OvertimeService();
        ReportService reportService = new ReportService(timeProvider);
        controller = new TimeTrackingController(attendanceService, breakService, overtimeService, reportService);
    }

    public void testMarkPunchIn() {
        Attendance attendance = controller.markPunchIn(1);
        if (attendance != null && attendance.getAttendanceId() == 1) {
            System.out.println("testMarkPunchIn: PASS");
        } else {
            System.out.println("testMarkPunchIn: FAIL");
        }
    }

    public void testMarkPunchOut() {
        controller.markPunchIn(1);
        try {
            controller.markPunchOut(1);
            System.out.println("testMarkPunchOut: PASS");
        } catch (Exception e) {
            System.out.println("testMarkPunchOut: FAIL");
        }
    }

    public void testStartAndEndBreak() {
        BreakRecord br = controller.startBreak(101);
        if (br != null) {
            try {
                controller.endBreak(br);
                System.out.println("testStartAndEndBreak: PASS");
            } catch (Exception e) {
                System.out.println("testStartAndEndBreak: FAIL");
            }
        } else {
            System.out.println("testStartAndEndBreak: FAIL");
        }
    }

    public void testGenerateReport() {
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(new Attendance(1, java.time.LocalDate.now(), java.time.LocalDateTime.now()));
        Report report = controller.generateReport(attendances);
        if (report != null && report.getTotalHours() >= 0) {
            System.out.println("testGenerateReport: PASS");
        } else {
            System.out.println("testGenerateReport: FAIL");
        }
    }

    public static void main(String[] args) {
        TimeTrackingControllerTest test = new TimeTrackingControllerTest();

        test.setUp();
        test.testMarkPunchIn();

        // Re-init so tests don't share repository state
        test.setUp();
        test.testMarkPunchOut();

        test.setUp();
        test.testStartAndEndBreak();

        test.setUp();
        test.testGenerateReport();
    }

    // Reuse the test repo
    private static class TestAttendanceRepository implements IAttendanceRepository {
        private java.util.Map<Integer, Attendance> attendances = new java.util.HashMap<>();

        @Override
        public void save(Attendance attendance) {
            attendances.put(attendance.getAttendanceId(), attendance);
        }

        @Override
        public Attendance findById(int id) {
            return attendances.get(id);
        }

        @Override
        public List<Attendance> findAll() {
            return new ArrayList<>(attendances.values());
        }

        @Override
        public void delete(int id) {
            attendances.remove(id);
        }
    }
}