package com.quangtung.springtest.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangtung.springtest.entity.Employee;
import com.quangtung.springtest.repository.EmployeeRepository;
import com.quangtung.springtest.service.impl.EmployeeServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setUp() throws Exception {
        employee = Employee.builder()
                .email("tung@gmail.com")
                .lastName("tung")
                .firstName("tran")
                .build();

        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("test for save employee")
    public void givenE_whenSE_thenNE() throws Exception {
        //given

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("test for get employee")
    public void given_whenGE_thenLE() throws Exception {
        //given
        employeeRepository.save(employee);
        List<Employee> employeeList = List.of(employee);
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(employeeList.size())));
    }

    @Test
    @DisplayName("test for get employee by id")
    public void given_when_then() throws Exception {
        //given
        employeeRepository.save(employee);
        Long id = 15L;
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",id));
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("test for update employee")
    public void givenE_whenUE_thenE() throws Exception {
        //given
        Long id = 17L;
        employeeRepository.save(employee);
        Employee employee1 = Employee.builder()
                .email("tung2@gmail.com")
                .lastName("tung2")
                .firstName("tran2")
                .build();
        employeeRepository.save(employee1);
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())));

    }

    @Test
    @DisplayName("test for delete employee")
    public void givenId_whenDE_thenOK() throws Exception {
        //given
        Long id = 18L;
        employeeRepository.save(employee);
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",id));

        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$",CoreMatchers.is("delete successful")));
    }



}
