package com.timetracking.domain.model;

public class Employee {
    private int employeeId;
    private String name;
    private String department;
    private String role;
    private String email;
    private String phone;

    public Employee(int employeeId, String name, String department,
            String role, String email, String phone) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}