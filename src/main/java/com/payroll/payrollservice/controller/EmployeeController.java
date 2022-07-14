package com.payroll.payrollservice.controller;

import com.payroll.payrollservice.entity.Employee;
import com.payroll.payrollservice.exception.EmployeeNotFoundException;
import com.payroll.payrollservice.pojo.EmployeeDto;
import com.payroll.payrollservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployee() {
        return ResponseEntity.ok(employeeService.getAllEmployees().stream().map(this::entityToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = null;
        try{
            employeeDto = entityToDto(employeeService.findEmployeeById(id));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entityToDto(employeeService.saveEmployee(dtoToEntity(employeeDto))));
    }

    @PutMapping(path = "/{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto input, @PathVariable Long id) {
        EmployeeDto employeeDto = null;
        try{
            employeeDto = entityToDto(employeeService.updateEmployee(dtoToEntity(input), id));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeDto);
    }

    private EmployeeDto entityToDto(Employee entity) {
        if(Objects.isNull(entity)) {
            return null;
        }
        EmployeeDto dto = modelMapper.map(entity, EmployeeDto.class);
        return dto;
    }

    private Employee dtoToEntity(EmployeeDto employeeDto) {
        if(Objects.isNull(employeeDto)) {
            return null;
        }
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        return employee;
    }

}
