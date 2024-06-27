package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.payload.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    public List<Employee> getAllEmployees() {
        return new ArrayList<>();
    }
}
