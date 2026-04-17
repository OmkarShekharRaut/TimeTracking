package com.timetracking.repository;

import com.timetracking.domain.model.Employee;
import java.util.List;

public interface IEmployeeRepository {
    void save(Employee employee);

    Employee findById(int id);

    List<Employee> findAll();

    void delete(int id);
}
