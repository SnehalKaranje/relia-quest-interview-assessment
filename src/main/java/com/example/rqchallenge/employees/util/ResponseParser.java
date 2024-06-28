package com.example.rqchallenge.employees.util;

import com.example.rqchallenge.employees.payload.Employee;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ResponseParser {
    @Autowired
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<Employee> parseToEmployeeList(JsonNode response) {
        List<Employee> employees = new ArrayList<>();

        JsonNode data = getData(response);
        for(JsonNode d : data) {
            employees.add(objectMapper.convertValue(d, Employee.class));
        }

        return employees;
    }

    public static Employee parseToEmployeeOb(JsonNode response) {
        JsonNode data = getData(response);
        return objectMapper.convertValue(data, Employee.class);
    }

    private static JsonNode getData(JsonNode response) {
        if ("success".equalsIgnoreCase(response.path("status").asText())) {
            return response.path("data");
        }
        return null;
    }
}
