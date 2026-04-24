package com.timetracking.reporting;

import java.time.LocalDateTime;

public class ExceptionReportEntry {
    private final LocalDateTime timestamp;
    private final String operation;
    private final String exceptionType;
    private final String message;
    private final String stackTrace;

    public ExceptionReportEntry(
            LocalDateTime timestamp,
            String operation,
            String exceptionType,
            String message,
            String stackTrace) {
        this.timestamp = timestamp;
        this.operation = operation;
        this.exceptionType = exceptionType;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getOperation() {
        return operation;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}

