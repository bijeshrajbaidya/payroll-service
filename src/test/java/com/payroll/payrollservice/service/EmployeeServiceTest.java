package com.payroll.payrollservice.service;

import com.payroll.payrollservice.entity.Employee;
import com.payroll.payrollservice.exception.EmployeeNotFoundException;
import com.payroll.payrollservice.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    ArgumentCaptor<Employee> employeeArgumentCaptor;

    @Test
    public void testFindAll() {

        //Act
        employeeService.getAllEmployees();

        //Assert
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testFindByIdWhenFound() throws EmployeeNotFoundException {
        //Arrange
        Long id = Long.valueOf(35);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        //Act
        Employee result = employeeService.findEmployeeById(35L);

        //Assert
        assertEquals(employee, result);
        verify(employeeRepository,times(1)).findById(longArgumentCaptor.capture());
        assertEquals(id, longArgumentCaptor.getValue());
    }

    @Test
    public void testFindByIdWhenNotFound() throws EmployeeNotFoundException {
        //Arrange
        Long id = Long.valueOf(36);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Act
        try {
            Employee result = employeeService.findEmployeeById(id);
        }catch (EmployeeNotFoundException e) {
            //Assert
            verify(employeeRepository,times(1)).findById(longArgumentCaptor.capture());
            assertEquals(id, longArgumentCaptor.getValue());
            assertEquals("No Employee record found with id=36", e.getMessage());
        }

    }

    @Test
    public void testAddEmployee() throws EmployeeNotFoundException {
        //Arrange
        Long id = Long.valueOf(350);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");

        Employee input = new Employee();
        input.setName("dipak");
        input.setJobRole("dev");

        when(employeeRepository.save(any())).thenReturn(employee);
        //Act
        Employee result = employeeService.saveEmployee(input);

        //Assert
        assertEquals(employee, result);
        verify(employeeRepository,times(1)).save(employeeArgumentCaptor.capture());
        assertEquals(input, employeeArgumentCaptor.getValue());
    }

    @Test
    public void testUpdateEmployee() throws EmployeeNotFoundException {
        //Arrange
        Long id = Long.valueOf(350);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("dipak");
        employee.setJobRole("dev");

        Employee input = new Employee();
        input.setName("dipak1");
        input.setJobRole("test");
        input.setId(id);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any())).thenReturn(input);
        //Act
        Employee result = employeeService.updateEmployee(input, id);

        //Assert
        assertEquals(input, result);
        verify(employeeRepository,times(1)).save(employeeArgumentCaptor.capture());
        assertEquals(input, employeeArgumentCaptor.getValue());

        verify(employeeRepository,times(1)).findById(longArgumentCaptor.capture());
        assertEquals(id, longArgumentCaptor.getValue());
    }

    @Test
    public void testUpdateEmployeeNotFound() throws EmployeeNotFoundException {
        //Arrange
        Long id = Long.valueOf(350);


        Employee input = new Employee();
        input.setName("dipak1");
        input.setJobRole("test");
        input.setId(id);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            //Act
            Employee result = employeeService.updateEmployee(input, id);
        }catch (EmployeeNotFoundException e) {
            //Assert
            verify(employeeRepository,times(0)).save(any());
            verify(employeeRepository,times(1)).findById(longArgumentCaptor.capture());
            assertEquals(id, longArgumentCaptor.getValue());
            assertEquals("No Employee record found with id=350", e.getMessage());
        }

    }


}
