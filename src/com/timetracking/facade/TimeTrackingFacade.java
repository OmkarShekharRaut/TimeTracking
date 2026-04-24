package com.timetracking.facade;

import com.timetracking.controller.TimeTrackingController;
import com.timetracking.domain.model.Attendance;
import com.timetracking.domain.model.BreakRecord;
import com.timetracking.domain.model.Report;
import com.timetracking.repository.IAttendanceRepository;

public class TimeTrackingFacade {
    private final TimeTrackingController controller;
    private final IAttendanceRepository attendanceRepository;

    public TimeTrackingFacade(TimeTrackingController controller, IAttendanceRepository attendanceRepository) {
        this.controller = controller;
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance punchIn(int attendanceId) {
        return controller.markPunchIn(attendanceId);
    }

    public Attendance punchOut(int attendanceId) {
        controller.markPunchOut(attendanceId);
        return attendanceRepository.findById(attendanceId);
    }

    public BreakRecord startBreak(int breakId) {
        return controller.startBreak(breakId);
    }

    public void endBreak(BreakRecord breakRecord) {
        controller.endBreak(breakRecord);
    }

    public Attendance findAttendanceById(int attendanceId) {
        return attendanceRepository.findById(attendanceId);
    }

    public Report generateReport() {
        return controller.generateReport(attendanceRepository.findAll());
    }
}
