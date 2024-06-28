package com.example.rqchallenge.employees.service.impl;

import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.employees.payload.Employee;
import com.example.rqchallenge.employees.repository.EmployeeRepository;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public JsonNode getAllEmployees() throws Exception {
        String responseStr = restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employees", String.class);
        return objectMapper.readTree(responseStr);
    }

    @Override
    public JsonNode getEmployeeById(String id) throws JsonProcessingException {
        String url = "https://dummy.restapiexample.com/api/v1/employee/" + id;
        String responseStr = restTemplate.getForObject(url, String.class);

        JsonNode node = objectMapper.readTree(responseStr);
        if (node == null || !"success".equalsIgnoreCase(node.path("status").asText())) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }
        return node;
    }

    @Override
    public JsonNode deleteEmployee(String id) throws JsonProcessingException {
        String url = "https://dummy.restapiexample.com/api/v1/employee/" + id;
        String responseStr = restTemplate.getForObject(url, String.class);
        JsonNode node = objectMapper.readTree(responseStr);

        if (node == null || !"success".equalsIgnoreCase(node.path("status").asText())) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }

        restTemplate.delete("https://dummy.restapiexample.com/api/v1/delete/" + id);

        ObjectNode objNode = objectMapper.createObjectNode();
        objNode.put("status", "success");
        objNode.put("message", "Successfully deleted record!");
        return objNode;
    }

    @Override
    public JsonNode createEmployee(String name, int salary, int age) {
        String url = "https://dummy.restapiexample.com/api/v1/create";
        Employee employee = restTemplate.postForObject(url, objectMapper.createObjectNode(), Employee.class);

        ObjectNode objNode = objectMapper.createObjectNode();
        objNode.put("status", "success");
        objNode.put("data", objectMapper.convertValue(employee, JsonNode.class));
        return objNode;
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        Employee emp = employeeRepository.findTopByOrderBySalaryDesc().get();
        return emp.getSalary();
    }

    @Override
    public List<String> getTop10HighestEarningEmployeeNames() {
        List<Employee> top10salaryEmployees = employeeRepository.findTop10ByOrderBySalaryDesc().get();
        return top10salaryEmployees.stream().map(employee -> employee.getName()).collect(Collectors.toList());
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchQuery) {
        List<Employee> employees = employeeRepository.searchEmployeesByName(searchQuery).get();
        return employees;
    }

    private JsonNode getData(String url) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String responseStr = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(responseStr);
    }
}
