package com.timetracking.test;

import com.timetracking.domain.model.BreakRecord;
import com.timetracking.service.BreakService;

public class BreakServiceTest {

    private BreakService breakService = new BreakService();

    public void testStartBreak() {
        BreakRecord breakRecord = breakService.startBreak(101);
        if (breakRecord != null && breakRecord.getBreakId() == 101 && breakRecord.getBreakStartTime() != null) {
            System.out.println("testStartBreak: PASS");
        } else {
            System.out.println("testStartBreak: FAIL");
        }
    }

    public void testEndBreak() {
        BreakRecord breakRecord = breakService.startBreak(101);
        breakService.endBreak(breakRecord);
        if (breakRecord.getBreakEndTime() != null && breakRecord.getBreakDuration() >= 0) {
            System.out.println("testEndBreak: PASS");
        } else {
            System.out.println("testEndBreak: FAIL");
        }
    }

    public static void main(String[] args) {
        BreakServiceTest test = new BreakServiceTest();
        test.testStartBreak();
        test.testEndBreak();
    }
}