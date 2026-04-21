package com.timetracking.test;

import com.timetracking.repository.IAttendanceRepository;
import com.timetracking.service.AttendanceService;
import com.timetracking.service.BreakService;
import com.timetracking.domain.model.Attendance;
import com.timetracking.exception.AttendanceNotFoundException;
import com.timetracking.exception.InvalidBreakException;
import java.util.ArrayList;
import java.util.List;

public class ExceptionTest {

    private AttendanceService attendanceService;
    private BreakService breakService;

    public void setUp() {
        TestAttendanceRepository testRepo = new TestAttendanceRepository();
        attendanceService = new AttendanceService(testRepo);
        breakService = new BreakService();
    }

    public void testAttendanceNotFoundException() {
        try {
            // Attempting to punch out an ID that doesn't exist
            attendanceService.punchOut(999);
            System.out.println("testAttendanceNotFoundException: FAIL (Exception not thrown)");
        } catch (AttendanceNotFoundException e) {
            System.out.println("testAttendanceNotFoundException: PASS (" + e.getMessage() + ")");
        } catch (Exception e) {
            System.out.println("testAttendanceNotFoundException: FAIL (Wrong exception thrown: " + e.getClass().getName() + ")");
        }
    }

    public void testInvalidBreakException() {
        try {
            // Attempting to end a null break record
            breakService.endBreak(null);
            System.out.println("testInvalidBreakException: FAIL (Exception not thrown)");
        } catch (InvalidBreakException e) {
            System.out.println("testInvalidBreakException: PASS (" + e.getMessage() + ")");
        } catch (Exception e) {
            System.out.println("testInvalidBreakException: FAIL (Wrong exception thrown: " + e.getClass().getName() + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Starting Exception Tests ---");
        ExceptionTest test = new ExceptionTest();
        test.setUp();
        test.testAttendanceNotFoundException();
        test.testInvalidBreakException();
        System.out.println("--- Exception Tests Finished ---");
    }

    // Local Test Repository Implementation
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
