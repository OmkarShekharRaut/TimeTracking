package com.timetracking.factory;

import com.timetracking.controller.TimeTrackingController;
import com.timetracking.facade.TimeTrackingFacade;
import com.timetracking.reporting.FileExceptionReporter;
import com.timetracking.repository.HrmsAttendanceRepositoryAdapter;
import com.timetracking.repository.IAttendanceRepository;
import com.timetracking.service.AttendanceService;
import com.timetracking.service.BreakService;
import com.timetracking.service.OvertimeService;
import com.timetracking.service.ReportService;
import com.timetracking.time.MutableTimeProvider;

import java.time.LocalDateTime;

public final class TimeTrackingAppFactory {

    private TimeTrackingAppFactory() {
    }

    public static AppComponents createForUi() {
        IAttendanceRepository attendanceRepository = new HrmsAttendanceRepositoryAdapter();

        AttendanceService attendanceService = new AttendanceService(attendanceRepository);
        BreakService breakService = new BreakService();
        OvertimeService overtimeService = new OvertimeService();
        ReportService reportService = new ReportService();

        TimeTrackingController controller = new TimeTrackingController(
                attendanceService,
                breakService,
                overtimeService,
                reportService,
                new FileExceptionReporter("exception-report.txt"));

        TimeTrackingFacade facade = new TimeTrackingFacade(controller, attendanceRepository);
        return new AppComponents(controller, attendanceRepository, facade, null);
    }

    public static AppComponents createForDemo() {
        IAttendanceRepository attendanceRepository = new HrmsAttendanceRepositoryAdapter();
        MutableTimeProvider clock = new MutableTimeProvider(LocalDateTime.now());

        AttendanceService attendanceService = new AttendanceService(attendanceRepository, clock);
        BreakService breakService = new BreakService(clock);
        OvertimeService overtimeService = new OvertimeService();
        ReportService reportService = new ReportService(clock);

        TimeTrackingController controller = new TimeTrackingController(
                attendanceService,
                breakService,
                overtimeService,
                reportService,
                new FileExceptionReporter("exception-report.txt"));

        TimeTrackingFacade facade = new TimeTrackingFacade(controller, attendanceRepository);
        return new AppComponents(controller, attendanceRepository, facade, clock);
    }

    public static final class AppComponents {
        private final TimeTrackingController controller;
        private final IAttendanceRepository attendanceRepository;
        private final TimeTrackingFacade facade;
        private final MutableTimeProvider demoClock;

        public AppComponents(TimeTrackingController controller,
                             IAttendanceRepository attendanceRepository,
                             TimeTrackingFacade facade,
                             MutableTimeProvider demoClock) {
            this.controller = controller;
            this.attendanceRepository = attendanceRepository;
            this.facade = facade;
            this.demoClock = demoClock;
        }

        public TimeTrackingController getController() {
            return controller;
        }

        public IAttendanceRepository getAttendanceRepository() {
            return attendanceRepository;
        }

        public TimeTrackingFacade getFacade() {
            return facade;
        }

        public MutableTimeProvider getDemoClock() {
            return demoClock;
        }
    }
}
