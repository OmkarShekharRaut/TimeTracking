package com.timetracking.domain.model;

public class Overtime {
    private int overtimeId;
    private double overtimeHours;
    private boolean approvalStatus;

    public Overtime(int overtimeId, double overtimeHours) {
        this.overtimeId = overtimeId;
        this.overtimeHours = overtimeHours;
        this.approvalStatus = false;
    }

    public void approve() {
        approvalStatus = true;
    }

    public int getOvertimeId() {
        return overtimeId;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public boolean isApprovalStatus() {
        return approvalStatus;
    }
}