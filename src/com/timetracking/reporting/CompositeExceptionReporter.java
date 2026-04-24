package com.timetracking.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeExceptionReporter implements ExceptionReporter {
    private final List<ExceptionReporter> reporters;

    public CompositeExceptionReporter(ExceptionReporter... reporters) {
        this.reporters = new ArrayList<>();
        if (reporters != null) {
            this.reporters.addAll(Arrays.asList(reporters));
        }
    }

    @Override
    public void report(ExceptionReportEntry entry) {
        for (ExceptionReporter reporter : reporters) {
            if (reporter != null) {
                reporter.report(entry);
            }
        }
    }
}

