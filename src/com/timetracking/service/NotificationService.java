package com.timetracking.service;

import com.timetracking.domain.model.*;
import com.timetracking.time.SystemTimeProvider;
import com.timetracking.time.TimeProvider;

import java.util.Objects;

public class NotificationService {

    private final TimeProvider timeProvider;

    public NotificationService() {
        this(new SystemTimeProvider());
    }

    public NotificationService(TimeProvider timeProvider) {
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public Notification createNotification(int id, String message) {

        return new Notification(id, message, timeProvider.now());
    }

    public void sendNotification(Notification notification) {

        System.out.println("NOTIFICATION SENT");
    }
}