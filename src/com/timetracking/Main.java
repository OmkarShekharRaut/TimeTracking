package com.timetracking;

import com.timetracking.controller.*;
import com.timetracking.repository.*;
import com.timetracking.domain.model.*;
import com.timetracking.factory.TimeTrackingAppFactory;
import com.timetracking.factory.TimeTrackingAppFactory.AppComponents;
import com.timetracking.facade.TimeTrackingFacade;
import com.timetracking.ui.TimeTrackingUI;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean runDemo = args != null && args.length > 0 && "demo".equalsIgnoreCase(args[0]);
        AppComponents components = runDemo
                ? TimeTrackingAppFactory.createForDemo()
                : TimeTrackingAppFactory.createForUi();
        TimeTrackingController controller = components.getController();
        IAttendanceRepository repo = components.getAttendanceRepository();
        TimeTrackingFacade facade = components.getFacade();

        if (runDemo) {
            Attendance attendance = controller.markPunchIn(1);

            System.out.println("Punch In Successful");

            BreakRecord br = controller.startBreak(101);

            components.getDemoClock().advance(java.time.Duration.ofMinutes(2));

            controller.endBreak(br);

            attendance.addBreak(br);

            components.getDemoClock().advance(java.time.Duration.ofMinutes(8));

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
            new TimeTrackingUI(facade).setVisible(true);
        });
    }
}