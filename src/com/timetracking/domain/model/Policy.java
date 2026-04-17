package com.timetracking.domain.model;

public class Policy {
    private int policyId;
    private double standardWorkHours;
    private double overtimeThreshold;
    private double maxBreakDuration;

    public Policy(int policyId, double standardWorkHours,
            double overtimeThreshold, double maxBreakDuration) {
        this.policyId = policyId;
        this.standardWorkHours = standardWorkHours;
        this.overtimeThreshold = overtimeThreshold;
        this.maxBreakDuration = maxBreakDuration;
    }

    public int getPolicyId() {
        return policyId;
    }

    public double getStandardWorkHours() {
        return standardWorkHours;
    }

    public double getOvertimeThreshold() {
        return overtimeThreshold;
    }

    public double getMaxBreakDuration() {
        return maxBreakDuration;
    }
}