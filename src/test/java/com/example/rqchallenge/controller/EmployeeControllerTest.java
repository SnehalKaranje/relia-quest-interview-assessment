package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.payload.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void getAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("1", "snehal", 28, 3400000, ""));
        employees.add(new Employee("2", "sharmad", 30, 4000000, ""));

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
