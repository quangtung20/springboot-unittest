package com.quangtung.springtest.unit.repository;

import com.quangtung.springtest.entity.Employee;

import com.quangtung.springtest.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Rollback
class EmployeeRepositoryTest {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private Employee employee;
    
    @BeforeEach
    public void setUp() throws Exception {
        employee = Employee.builder()
                .firstName("quangtung")
                .lastName("tran")
                .email("tung@gmail.com")
                .build();
    }
    
    @Test
    @DisplayName("test for save employee")
    public void givenE_whenSE_thenNE(){
        //given
        
        //when
        Employee savedEmployee = employeeRepository.save(employee);
        //then
        System.out.println("savedEmployee = " + savedEmployee);
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("test for get all employees")
    public void given2E_whenGE_thenSGZ(){
        //given
        Employee employee1 = Employee.builder()
                .firstName("quangtung2")
                .lastName("tran2")
                .email("tung2@gmail.com")
                .build();

        List<Employee> employeeList = List.of(employee,employee1);
        employeeRepository.saveAll(employeeList);
        //when
        List<Employee> employeeListGetted = employeeRepository.findAll();
        //then
        System.out.println("employeeListGetted = " + employeeListGetted);
        assertThat(employeeListGetted.size()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("test for update employee")
    public void givenE_whenUE_thenNE(){
        //given
        Employee addEmployee = employeeRepository.save(employee);
        Employee updateEmployee = employeeRepository.findById(addEmployee.getId()).get();
        updateEmployee.setEmail("nam@gmail.com");
        //when
        employeeRepository.save(updateEmployee);
        //then
        Employee updatedEmployee = employeeRepository.findById(addEmployee.getId()).get();
        System.out.println("updatedEmployee = " + updatedEmployee);
        assertThat(updatedEmployee.getEmail()).isEqualTo("nam@gmail.com");
    }

    @Test
    @DisplayName("test for delete employee")
    public void givenE_whenDE_thenNE(){
        //given
        Employee savedEmployee = employeeRepository.save(employee);
        //when
        employeeRepository.deleteById(savedEmployee.getId());
        //then
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList.size()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("test jpql ??")
    public void givenE_whenFE_thenE(){
        //given
        Employee savedEmployee = employeeRepository.save(employee);
        //when
        Employee getEmployee = employeeRepository.findEmployeeByJpql("tran","quangtung").get();
        //then
        System.out.println("getEmployee = " + getEmployee);
        assertThat(getEmployee).isNotNull();
    }

    @Test
    @DisplayName("test for find by email")
    public void givenE_whenFBE_thenE(){
        //given
        employeeRepository.save(employee);
        //when
        Employee employee1 = employeeRepository.findEmployeeByEmail("tung@gmail.com").get();
        //then
        assertThat(employee1).isNotNull();
    }

    @Test
    @DisplayName("test for find by native query")
    public void givenE_whenSE_thenTRE(){
        //given
        employeeRepository.save(employee);
        //when
        Employee getEmployee = employeeRepository.findEmployeeByFirstNameNative("quangtung").get();
        //then
        assertThat(getEmployee.getFirstName()).isEqualTo("quangtung");
    }
}