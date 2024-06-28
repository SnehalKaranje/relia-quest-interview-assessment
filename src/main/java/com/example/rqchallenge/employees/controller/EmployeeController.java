package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.payload.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.employees.util.ResponseParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements IEmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws Exception {
        JsonNode response = employeeService.getAllEmployees();
        return new ResponseEntity<>(ResponseParser.parseToEmployeeList(response), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return new ResponseEntity<>(employeeService.getEmployeesByNameSearch(searchString), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) throws JsonProcessingException {
        JsonNode response = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(ResponseParser.parseToEmployeeOb(response), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return new ResponseEntity<>(employeeService.getHighestSalaryOfEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return new ResponseEntity<>(employeeService.getTop10HighestEarningEmployeeNames(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        JsonNode response = employeeService.createEmployee((String) employeeInput.get("name"),
                (Integer) employeeInput.get("salary"),
                (Integer) employeeInput.get("age"));

        return new ResponseEntity<>(ResponseParser.parseToEmployeeOb(response), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) throws JsonProcessingException {
        JsonNode node = employeeService.deleteEmployee(id);
        return new ResponseEntity<>(node.path("message").asText(), HttpStatus.OK);
    }
}
