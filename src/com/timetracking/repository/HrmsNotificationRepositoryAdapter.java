package com.timetracking.repository;

import com.timetracking.domain.model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsNotificationRepositoryAdapter implements INotificationRepository {

    private final com.hrms.db.repositories.timetracking.INotificationRepository delegate;
    private final Map<Integer, Notification> sessionCache = new ConcurrentHashMap<>();

    public HrmsNotificationRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingNotificationRepositoryImpl();
    }

    @Override
    public void save(Notification notification) {
        if (notification == null) {
            return;
        }
        sessionCache.put(notification.getNotificationId(), notification);
        delegate.save(toHrmsNotification(notification));
    }

    @Override
    public Notification findById(int id) {
        Notification cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }
        com.hrms.db.entities.Notification stored = delegate.findById((long) id);
        Notification mapped = toDomainNotification(stored);
        if (mapped != null) {
            sessionCache.put(mapped.getNotificationId(), mapped);
        }
        return mapped;
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete((long) id);
    }

    private com.hrms.db.entities.Notification toHrmsNotification(Notification notification) {
        com.hrms.db.entities.Notification mapped = new com.hrms.db.entities.Notification();
        mapped.setNotificationId((long) notification.getNotificationId());
        mapped.setNotificationMessage(notification.getMessage());
        mapped.setStatus(notification.getStatus());
        mapped.setBody(notification.getMessage());
        mapped.setSubject("TimeTracking Notification");
        return mapped;
    }

    private Notification toDomainNotification(com.hrms.db.entities.Notification notification) {
        if (notification == null || notification.getNotificationId() == null) {
            return null;
        }
        int id = notification.getNotificationId().intValue();
        LocalDateTime timestamp = notification.getCreatedAt() != null ? notification.getCreatedAt() : LocalDateTime.now();
        String message = notification.getNotificationMessage() != null ? notification.getNotificationMessage() : "";
        Notification mapped = new Notification(id, message, timestamp);
        if (notification.getStatus() != null) {
            mapped.setStatus(notification.getStatus());
        }
        return mapped;
    }
}
