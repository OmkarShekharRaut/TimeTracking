package com.timetracking.repository;

import com.timetracking.domain.model.Policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsPolicyRepositoryAdapter implements IPolicyRepository {

    private final com.hrms.db.repositories.timetracking.IPolicyRepository delegate;
    private final Map<Integer, Policy> sessionCache = new ConcurrentHashMap<>();

    public HrmsPolicyRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingPolicyRepositoryImpl();
    }

    @Override
    public void save(Policy policy) {
        if (policy == null) {
            return;
        }
        sessionCache.put(policy.getPolicyId(), policy);
        delegate.save(toHrmsPolicy(policy));
    }

    @Override
    public Policy findById(int id) {
        Policy cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }
        com.hrms.db.entities.WorkPolicy stored = delegate.findById(id);
        Policy mapped = toDomainPolicy(stored);
        if (mapped != null) {
            sessionCache.put(mapped.getPolicyId(), mapped);
        }
        return mapped;
    }

    @Override
    public List<Policy> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete(id);
    }

    private com.hrms.db.entities.WorkPolicy toHrmsPolicy(Policy policy) {
        com.hrms.db.entities.WorkPolicy mapped = new com.hrms.db.entities.WorkPolicy();
        mapped.setPolicyId(policy.getPolicyId());
        mapped.setPolicyName("Policy-" + policy.getPolicyId());
        mapped.setStandardWorkHours(policy.getStandardWorkHours());
        mapped.setOvertimeThreshold(policy.getOvertimeThreshold());
        mapped.setMaxBreakDuration(policy.getMaxBreakDuration());
        return mapped;
    }

    private Policy toDomainPolicy(com.hrms.db.entities.WorkPolicy workPolicy) {
        if (workPolicy == null || workPolicy.getPolicyId() == null) {
            return null;
        }
        return new Policy(
                workPolicy.getPolicyId(),
                workPolicy.getStandardWorkHours() != null ? workPolicy.getStandardWorkHours() : 0.0,
                workPolicy.getOvertimeThreshold() != null ? workPolicy.getOvertimeThreshold() : 0.0,
                workPolicy.getMaxBreakDuration() != null ? workPolicy.getMaxBreakDuration() : 0.0);
    }
}
