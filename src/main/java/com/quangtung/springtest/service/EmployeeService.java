package com.quangtung.springtest.service;

import com.quangtung.springtest.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id,Employee updatedEmployee);
    void deleteEmployee(Long id);
}
