package com.timetracking;

import com.timetracking.controller.*;
import com.timetracking.repository.*;
import com.timetracking.service.*;
import com.timetracking.domain.model.*;
import com.timetracking.time.MutableTimeProvider;
import com.timetracking.reporting.FileExceptionReporter;
import com.timetracking.ui.TimeTrackingUI;

public class Main {

    public static void main(String[] args) throws Exception {

        IAttendanceRepository repo = new MockAttendanceRepository();

        MutableTimeProvider clock = new MutableTimeProvider(java.time.LocalDateTime.now());

        AttendanceService attendanceService = new AttendanceService(repo, clock);
        BreakService breakService = new BreakService(clock);
        OvertimeService overtimeService = new OvertimeService();
        ReportService reportService = new ReportService(clock);

        TimeTrackingController controller = new TimeTrackingController(
                attendanceService,
                breakService,
                overtimeService,
                reportService,
                new FileExceptionReporter("exception-report.txt"));

        boolean runDemo = args != null && args.length > 0 && "demo".equalsIgnoreCase(args[0]);
        if (runDemo) {
            Attendance attendance = controller.markPunchIn(1);

            System.out.println("Punch In Successful");

            BreakRecord br = controller.startBreak(101);

            clock.advance(java.time.Duration.ofMinutes(2));

            controller.endBreak(br);

            attendance.addBreak(br);

            clock.advance(java.time.Duration.ofMinutes(8));

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
            return;
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            new TimeTrackingUI(controller, repo).setVisible(true);
        });
    }
}