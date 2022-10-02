package com.quangtung.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangtung.springtest.entity.Employee;
import com.quangtung.springtest.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
@Rollback
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("test for add employee ctrl")
    public void givenE_whenE_thenE() throws Exception {
        //given
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)) // content = body
        );
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName("test for get employee")
    public void given_when_then() throws Exception {
        //given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("tung").lastName("tran").email("tung@gmail.com").build());
        employeeList.add(Employee.builder().firstName("nam").lastName("tran").email("nam@gmail.com").build());
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);
        //when
        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(employeeList.size())));
        //then
    }

    @Test
    @DisplayName("test for get employee by id")
    public void givenE_whenE_thenES() throws Exception {
        //given
        Long id = 1L;
        BDDMockito.given(employeeService.getEmployeeById(id)).willReturn(employee);
        //when
        ResultActions reponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",id));
        //then
        reponse.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("test for update controller")
    public void givenE_whenUE_thenE() throws Exception {
        //given
        Long id = 1L;

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.eq(id),ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(1)));
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName("test for delete employee")
    public void givenId_whenDE_thenOK() throws Exception {
        //given
        Long id = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(id);
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",id));

        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$",CoreMatchers.is("delete successful")));
    }

}