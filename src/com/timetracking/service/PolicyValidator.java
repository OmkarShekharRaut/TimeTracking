package com.timetracking.service;

import com.timetracking.domain.model.*;

public class PolicyValidator {

    public boolean validateAttendance(Attendance attendance, Policy policy) {
        return attendance.getTotalWorkHours() >= policy.getStandardWorkHours();
    }
}
