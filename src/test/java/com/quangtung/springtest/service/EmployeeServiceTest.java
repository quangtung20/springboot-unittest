package com.quangtung.springtest.service;

import com.quangtung.springtest.entity.Employee;
import com.quangtung.springtest.exception.ResourceNotFoundException;
import com.quangtung.springtest.repository.EmployeeRepository;
import com.quangtung.springtest.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Rollback
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() throws Exception {
        employee = Employee.builder()
                .id(1L)
                .firstName("quangtung")
                .lastName("tran")
                .email("tung@gmail.com")
                .build();

        employeeRepository.save(employee);
    }

    @Test
    @DisplayName("test for save service")
    public void givenE_whenSE_thenNN(){
        //given
        BDDMockito.given(employeeRepository.findEmployeeByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //then
        System.out.println("savedEmployee = " + savedEmployee);
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("test for save service with throw")
    public void given_when_then(){
        //given
        BDDMockito.given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        //then
    }

    @Test
    @DisplayName("test for get all by employeeservice")
    public void givenL_whenG_thenRL(){
        //given
        Employee employee1 = Employee.builder()
                .firstName("quangtung2")
                .lastName("tran2")
                .email("tung2@gmail.com")
                .build();

        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
        //when
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("test for employeeService.getEmployeeById")
    public void givenE_whenFE_thenE(){
        //given
        Long id = 1L;
        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when
        Employee savedEmployee = employeeService.getEmployeeById(id);
        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("test for delete")
    public void givenId_whenD_thenN(){
        //given
        Long id = 1L;
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(id);
        //when
        employeeService.deleteEmployee(id);
        //then
        BDDMockito.verify(employeeRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("test for update exception")
    public void givenE_whenUE_thenEX(){
        //given
        Long id = 1L;
        BDDMockito.given(employeeRepository.findById(id)).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->employeeService.updateEmployee(id,employee));

        //then
        BDDMockito.verify(employeeRepository, Mockito.times(1)).save(employee);
    }

    @Test
    @DisplayName("test for update done")
    public void givenE_whenUE_thenE(){
        //given
        Long id = 1L;
        BDDMockito.given(employeeRepository.findById(id)).willReturn(Optional.of(employee));
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when
        Employee saveEmployee = employeeService.updateEmployee(id,employee);
        //then
        BDDMockito.verify(employeeRepository, Mockito.times(2)).save(employee);
    }
}