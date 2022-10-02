package com.quangtung.springtest.service.impl;

import com.quangtung.springtest.entity.Employee;
import com.quangtung.springtest.exception.ResourceNotFoundException;
import com.quangtung.springtest.repository.EmployeeRepository;
import com.quangtung.springtest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> checkEmployee = employeeRepository.findEmployeeByEmail("tung@gmail.com");
        if(checkEmployee.isPresent()){
            throw new ResourceNotFoundException("employee has already exists");
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return  savedEmployee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        return employee;
    }

    @Override
    public Employee updateEmployee(Long id,Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new ResourceNotFoundException("employee does not exists");
        }
        Employee saveEmployee = employeeRepository.save(updatedEmployee);
        return saveEmployee;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
