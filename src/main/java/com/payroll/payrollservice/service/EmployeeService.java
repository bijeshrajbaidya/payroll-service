package com.payroll.payrollservice.service;

import com.payroll.payrollservice.entity.Employee;
import com.payroll.payrollservice.exception.EmployeeNotFoundException;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee findEmployeeById(Long id) throws EmployeeNotFoundException;

    Employee saveEmployee(Employee employeeDto);

    Employee updateEmployee(Employee employeeDto, Long id) throws EmployeeNotFoundException;
}
