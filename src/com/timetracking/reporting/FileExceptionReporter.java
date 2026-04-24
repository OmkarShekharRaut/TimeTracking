package com.timetracking.reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FileExceptionReporter implements ExceptionReporter {
    private final String filePath;

    public FileExceptionReporter(String filePath) {
        this.filePath = Objects.requireNonNull(filePath, "filePath");
    }

    @Override
    public void report(ExceptionReportEntry entry) {
        if (entry == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("=== Exception Report ===\n");
            writer.write("Time: " + entry.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n");
            writer.write("Operation: " + entry.getOperation() + "\n");
            writer.write("Type: " + entry.getExceptionType() + "\n");
            writer.write("Message: " + entry.getMessage() + "\n");
            writer.write("StackTrace:\n" + entry.getStackTrace() + "\n");
            writer.write("\n");
        } catch (IOException ignored) {
            // Avoid throwing from the reporter; controller should keep running.
        }
    }
}

