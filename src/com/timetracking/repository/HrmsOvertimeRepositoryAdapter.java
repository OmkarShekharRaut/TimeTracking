package com.timetracking.repository;

import com.timetracking.domain.model.Overtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsOvertimeRepositoryAdapter implements IOvertimeRepository {

    private final com.hrms.db.repositories.timetracking.IOvertimeRepository delegate;
    private final Map<Integer, Overtime> sessionCache = new ConcurrentHashMap<>();

    public HrmsOvertimeRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingOvertimeRepositoryImpl();
    }

    @Override
    public void save(Overtime overtime) {
        if (overtime == null) {
            return;
        }
        sessionCache.put(overtime.getOvertimeId(), overtime);
        delegate.save(toHrmsOvertime(overtime));
    }

    @Override
    public Overtime findById(int id) {
        Overtime cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }
        com.hrms.db.entities.OvertimeRecord stored = delegate.findById((long) id);
        Overtime mapped = toDomainOvertime(stored);
        if (mapped != null) {
            sessionCache.put(mapped.getOvertimeId(), mapped);
        }
        return mapped;
    }

    @Override
    public List<Overtime> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete((long) id);
    }

    private com.hrms.db.entities.OvertimeRecord toHrmsOvertime(Overtime overtime) {
        com.hrms.db.entities.OvertimeRecord mapped = new com.hrms.db.entities.OvertimeRecord();
        mapped.setOvertimeId((long) overtime.getOvertimeId());
        mapped.setOvertimeHours(overtime.getOvertimeHours());
        mapped.setApprovalStatus(overtime.isApprovalStatus() ? "Approved" : "Pending");
        return mapped;
    }

    private Overtime toDomainOvertime(com.hrms.db.entities.OvertimeRecord overtimeRecord) {
        if (overtimeRecord == null || overtimeRecord.getOvertimeId() == null) {
            return null;
        }
        Overtime mapped = new Overtime(overtimeRecord.getOvertimeId().intValue(),
                overtimeRecord.getOvertimeHours() != null ? overtimeRecord.getOvertimeHours() : 0.0);

        String status = overtimeRecord.getApprovalStatus();
        if (status != null && ("approved".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status))) {
            mapped.approve();
        }
        return mapped;
    }
}
