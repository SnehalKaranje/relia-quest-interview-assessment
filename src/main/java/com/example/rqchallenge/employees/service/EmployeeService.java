package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.payload.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface EmployeeService {
    public JsonNode getAllEmployees() throws Exception;

    public JsonNode getEmployeeById(String id) throws JsonProcessingException;

    public JsonNode deleteEmployee(String id) throws JsonProcessingException;

    public JsonNode createEmployee(String name, int salary, int age);

    public int getHighestSalaryOfEmployees();

    public List<String> getTop10HighestEarningEmployeeNames();

    public List<Employee> getEmployeesByNameSearch(String searchQuery);
}
