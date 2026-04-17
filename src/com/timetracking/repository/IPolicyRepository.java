package com.timetracking.repository;

import com.timetracking.domain.model.Policy;
import java.util.List;

public interface IPolicyRepository {
    void save(Policy policy);

    Policy findById(int id);

    List<Policy> findAll();

    void delete(int id);
}
