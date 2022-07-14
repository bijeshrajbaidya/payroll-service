package com.payroll.payrollservice.service;

import com.payroll.payrollservice.entity.Employee;
import com.payroll.payrollservice.exception.EmployeeNotFoundException;
import com.payroll.payrollservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        throw new EmployeeNotFoundException("No Employee record found with id="+ id);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, Long id) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            employee.setId(id);
            return employeeRepository.save(employee);
        }
        throw new EmployeeNotFoundException("No Employee record found with id="+ id);
    }

}
