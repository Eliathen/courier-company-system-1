package com.github.haseoo.courier.repositories.adapters;

import com.github.haseoo.courier.models.EmployeeModel;
import com.github.haseoo.courier.repositories.jpa.EmployeeJPARepository;
import com.github.haseoo.courier.repositories.ports.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final EmployeeJPARepository employeeJPARepository;

    @Override
    public List<EmployeeModel> getList() {
        return employeeJPARepository.findAll();
    }
}