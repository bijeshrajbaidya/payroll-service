package com.payroll.payrollservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String jobRole;

}
