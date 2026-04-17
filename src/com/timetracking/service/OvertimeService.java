package com.timetracking.service;

import com.timetracking.domain.model.*;

public class OvertimeService {

    public Overtime calculateOvertime(Attendance attendance, Policy policy) {

        double overtimeHours = attendance.getTotalWorkHours() - policy.getOvertimeThreshold();

        if (overtimeHours > 0) {
            return new Overtime(1, overtimeHours);
        }

        return null;
    }
}