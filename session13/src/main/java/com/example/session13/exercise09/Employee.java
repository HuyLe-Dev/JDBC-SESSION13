package com.example.session13.exercise09;

import java.math.BigDecimal;

public class Employee {
    private String name;
    private String department;
    private BigDecimal salary;

    public Employee(String name, String department, BigDecimal salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
