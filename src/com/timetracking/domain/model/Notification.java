package com.timetracking.domain.model;

import java.time.*;

public class Notification {
    private int notificationId;
    private String message;
    private LocalDateTime timestamp;
    private String status;

    public Notification(int id, String message) {
        this.notificationId = id;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = "Unread";
    }

    public Notification(int id, String message, LocalDateTime timestamp) {
        this.notificationId = id;
        this.message = message;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.status = "Unread";
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}