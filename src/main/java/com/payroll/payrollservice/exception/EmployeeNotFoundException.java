package com.payroll.payrollservice.exception;

public class EmployeeNotFoundException extends Exception{
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
