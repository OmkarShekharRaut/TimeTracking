package com.timetracking.repository;

import com.timetracking.domain.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HrmsEmployeeRepositoryAdapter implements IEmployeeRepository {

    private final com.hrms.db.repositories.timetracking.IEmployeeRepository delegate;
    private final Map<Integer, Employee> sessionCache = new ConcurrentHashMap<>();

    public HrmsEmployeeRepositoryAdapter() {
        this.delegate = new com.hrms.db.repositories.timetracking.TimeTrackingEmployeeRepositoryImpl();
    }

    @Override
    public void save(Employee employee) {
        if (employee == null) {
            return;
        }
        sessionCache.put(employee.getEmployeeId(), employee);
        delegate.save(toHrmsEmployee(employee));
    }

    @Override
    public Employee findById(int id) {
        Employee cached = sessionCache.get(id);
        if (cached != null) {
            return cached;
        }
        com.hrms.db.entities.Employee stored = delegate.findById(String.valueOf(id));
        Employee mapped = toDomainEmployee(stored);
        if (mapped != null) {
            sessionCache.put(mapped.getEmployeeId(), mapped);
        }
        return mapped;
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(sessionCache.values());
    }

    @Override
    public void delete(int id) {
        sessionCache.remove(id);
        delegate.delete(String.valueOf(id));
    }

    private com.hrms.db.entities.Employee toHrmsEmployee(Employee employee) {
        com.hrms.db.entities.Employee mapped = new com.hrms.db.entities.Employee();
        mapped.setEmpId(String.valueOf(employee.getEmployeeId()));
        mapped.setName(employee.getName());
        mapped.setDepartment(employee.getDepartment());
        mapped.setRole(employee.getRole());
        mapped.setEmail(employee.getEmail());
        mapped.setPhone(employee.getPhone());
        return mapped;
    }

    private Employee toDomainEmployee(com.hrms.db.entities.Employee employee) {
        if (employee == null || employee.getEmpId() == null) {
            return null;
        }
        int id;
        try {
            id = Integer.parseInt(employee.getEmpId());
        } catch (NumberFormatException ex) {
            return null;
        }
        return new Employee(
                id,
                employee.getName(),
                employee.getDepartment(),
                employee.getRole(),
                employee.getEmail(),
                employee.getPhone());
    }
}
