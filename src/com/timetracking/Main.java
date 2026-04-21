package com.timetracking;

import com.timetracking.controller.*;
import com.timetracking.repository.*;
import com.timetracking.service.*;
import com.timetracking.domain.model.*;

public class Main {

    public static void main(String[] args) throws Exception {

        IAttendanceRepository repo = new MockAttendanceRepository();

        AttendanceService attendanceService = new AttendanceService(repo);
        BreakService breakService = new BreakService();
        OvertimeService overtimeService = new OvertimeService();
        ReportService reportService = new ReportService();

        TimeTrackingController controller = new TimeTrackingController(
                attendanceService,
                breakService,
                overtimeService,
                reportService);

        Attendance attendance = controller.markPunchIn(1);

        System.out.println("Punch In Successful");

        BreakRecord br = controller.startBreak(101);

        Thread.sleep(2000);

        controller.endBreak(br);

        attendance.addBreak(br);

        Thread.sleep(3000);

        controller.markPunchOut(1);

        System.out.println("Punch Out Successful");

        System.out.println("Hours Worked: " +
                repo.findById(1).getTotalWorkHours());

        Report report = controller.generateReport(repo.findAll());

        System.out.println("Generated Report Hours: " +
                report.getTotalHours());

        System.out.println("\n--- Testing Exception Handling ---");
        System.out.println("Attempting to punch out an invalid ID...");
        controller.markPunchOut(999);

        System.out.println("Attempting to end a null break record...");
        controller.endBreak(null);
    }
}