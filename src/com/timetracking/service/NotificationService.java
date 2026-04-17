package com.timetracking.service;

import com.timetracking.domain.model.*;

public class NotificationService {

    public Notification createNotification(int id, String message) {

        return new Notification(id, message);
    }

    public void sendNotification(Notification notification) {

        System.out.println("NOTIFICATION SENT");
    }
}