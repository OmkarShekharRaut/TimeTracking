package com.timetracking.repository;

import com.timetracking.domain.model.BreakRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsBreakRepositoryAdapter implements IBreakRepository {

    private final com.hrms.db.repositories.timetracking.IBreakRepository delegate;
    private final Map<Integer, BreakRecord> sessionCache = new ConcurrentHashMap<>();

    public HrmsBreakRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingBreakRepositoryImpl();
    }

    @Override
    public void save(BreakRecord breakRecord) {
        if (breakRecord == null) {
            return;
        }
        sessionCache.put(breakRecord.getBreakId(), breakRecord);
        delegate.save(toHrmsBreakRecord(breakRecord));
    }

    @Override
    public BreakRecord findById(int id) {
        BreakRecord cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }

        com.hrms.db.entities.BreakRecord stored = delegate.findById((long) id);
        BreakRecord mapped = toDomainBreakRecord(stored);
        if (mapped != null) {
            sessionCache.put(id, mapped);
        }
        return mapped;
    }

    @Override
    public List<BreakRecord> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete((long) id);
    }

    private com.hrms.db.entities.BreakRecord toHrmsBreakRecord(BreakRecord breakRecord) {
        com.hrms.db.entities.BreakRecord mapped = new com.hrms.db.entities.BreakRecord();
        mapped.setBreakId((long) breakRecord.getBreakId());
        mapped.setEntryId((long) breakRecord.getBreakId());
        mapped.setBreakStartTime(breakRecord.getBreakStartTime());
        mapped.setBreakEndTime(breakRecord.getBreakEndTime());
        return mapped;
    }

    private BreakRecord toDomainBreakRecord(com.hrms.db.entities.BreakRecord breakRecord) {
        if (breakRecord == null || breakRecord.getBreakId() == null || breakRecord.getBreakStartTime() == null) {
            return null;
        }
        int id = breakRecord.getBreakId().intValue();
        BreakRecord mapped = new BreakRecord(id, breakRecord.getBreakStartTime());
        if (breakRecord.getBreakEndTime() != null && !breakRecord.getBreakEndTime().isBefore(breakRecord.getBreakStartTime())) {
            mapped.endBreak(breakRecord.getBreakEndTime());
        }
        return mapped;
    }
}
