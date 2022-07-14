package com.payroll.payrollservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.payrollservice.entity.Employee;
import com.payroll.payrollservice.exception.EmployeeNotFoundException;
import com.payroll.payrollservice.service.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @MockBean
    EmployeeService employeeService;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    ArgumentCaptor<Employee> employeeArgumentCaptor;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testfindAll() throws Exception {
        Long id = Long.valueOf(350);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");
        List<Employee> employees = Arrays.asList(employee);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("dipak")))
                .andExpect(jsonPath("$[0].jobRole", Matchers.is("dev")))
                .andExpect(jsonPath("$[0].id", Matchers.is(350)));
    }

    @Test
    public void testfindById() throws Exception {

        Long id = Long.valueOf(350);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");

        when(employeeService.findEmployeeById(any())).thenReturn(employee);

        mockMvc.perform(get("/employee/56"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("dipak")))
                .andExpect(jsonPath("$.jobRole", Matchers.is("dev")))
                .andExpect(jsonPath("$.id", Matchers.is(350)));
        verify(employeeService, times(1)).findEmployeeById(longArgumentCaptor.capture());
        assertEquals(56,longArgumentCaptor.getValue());
    }

    @Test
    public void testfindByIdWhenException() throws Exception {

        when(employeeService.findEmployeeById(any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get("/employee/56"))
                .andExpect(status().isNotFound());
        verify(employeeService, times(1)).findEmployeeById(longArgumentCaptor.capture());
        assertEquals(56,longArgumentCaptor.getValue());
    }

    @Test
    public void testAddEmployee() throws Exception {

        Long id = Long.valueOf(350);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");

        Employee input = new Employee();
        input.setName("dipak");
        input.setJobRole("dev");

        when(employeeService.saveEmployee(any())).thenReturn(employee);

        mockMvc.perform(post("/employee").content(objectMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("dipak")))
                .andExpect(jsonPath("$.jobRole", Matchers.is("dev")))
                .andExpect(jsonPath("$.id", Matchers.is(350)));
        verify(employeeService, times(1)).saveEmployee(employeeArgumentCaptor.capture());
        assertEquals(input,employeeArgumentCaptor.getValue());
    }

    @Test
    public void testUpdateEmployee() throws Exception {

        Long id = Long.valueOf(350);

        Employee input = new Employee();
        input.setId(id);
        input.setName("dipak1");
        input.setJobRole("test");

        when(employeeService.updateEmployee(any(),any())).thenReturn(input);

        mockMvc.perform(put("/employee/56").content(objectMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("dipak1")))
                .andExpect(jsonPath("$.jobRole", Matchers.is("test")))
                .andExpect(jsonPath("$.id", Matchers.is(350)));
        verify(employeeService, times(1)).updateEmployee(employeeArgumentCaptor.capture(), longArgumentCaptor.capture());
        assertEquals(input,employeeArgumentCaptor.getValue());
        assertEquals(56,longArgumentCaptor.getValue());
    }

    @Test
    public void testUpdateEmployeeWhenException() throws Exception {

        Long id = Long.valueOf(350);

        Employee input = new Employee();
        input.setId(id);
        input.setName("dipak1");
        input.setJobRole("test");

        when(employeeService.updateEmployee(any(),any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(put("/employee/56").content(objectMapper.writeValueAsString(input)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
        verify(employeeService, times(1)).updateEmployee(employeeArgumentCaptor.capture(), longArgumentCaptor.capture());
        assertEquals(input,employeeArgumentCaptor.getValue());
        assertEquals(56,longArgumentCaptor.getValue());
    }


}
