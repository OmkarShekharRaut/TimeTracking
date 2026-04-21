package com.timetracking.test;

import com.timetracking.domain.model.Attendance;
import com.timetracking.repository.IAttendanceRepository;
import com.timetracking.service.AttendanceService;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServiceTest {

    private AttendanceService attendanceService;
    private TestAttendanceRepository testRepo;

    public void setUp() {
        testRepo = new TestAttendanceRepository();
        attendanceService = new AttendanceService(testRepo);
    }

    public void testPunchIn() {
        Attendance attendance = attendanceService.punchIn(1);
        if (attendance != null && attendance.getAttendanceId() == 1 && attendance.getPunchInTime() != null) {
            System.out.println("testPunchIn: PASS");
        } else {
            System.out.println("testPunchIn: FAIL");
        }
    }

    public void testPunchOut() {
        attendanceService.punchIn(1);
        attendanceService.punchOut(1);
        // Fetch updated attendance from repository
        Attendance updated = testRepo.findById(1);
        if (updated != null && updated.getPunchOutTime() != null && updated.getTotalWorkHours() >= 0) {
            System.out.println("testPunchOut: PASS");
        } else {
            System.out.println("testPunchOut: FAIL");
        }
    }

    public static void main(String[] args) {
        AttendanceServiceTest test = new AttendanceServiceTest();
        test.setUp();
        test.testPunchIn();
        test.testPunchOut();
    }

    // Simple mock repository for testing
    private static class TestAttendanceRepository implements IAttendanceRepository {
        private List<Attendance> attendances = new ArrayList<>();

        @Override
        public void save(Attendance attendance) {
            attendances.add(attendance);
        }

        @Override
        public Attendance findById(int id) {
            return attendances.stream()
                    .filter(a -> a.getAttendanceId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Attendance> findAll() {
            return new ArrayList<>(attendances);
        }

        @Override
        public void delete(int id) {
            attendances.removeIf(a -> a.getAttendanceId() == id);
        }
    }
}