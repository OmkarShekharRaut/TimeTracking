package com.timetracking.repository;

import com.timetracking.domain.model.Notification;
import java.util.List;

public interface INotificationRepository {
    void save(Notification notification);

    Notification findById(int id);

    List<Notification> findAll();

    void delete(int id);
}
