package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.controller.EmployeeController;
import com.example.rqchallenge.employees.payload.Employee;
import com.example.rqchallenge.employees.repository.EmployeeRepository;
import com.example.rqchallenge.employees.service.impl.EmployeeServiceImpl;
import com.example.rqchallenge.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
@Import({EmployeeController.class, EmployeeServiceImpl.class})
public class EmployeeControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetAllEmployees() throws Exception {
        JsonNode node = FileUtil.readFromFile("/all_employees_dummy_response.json");
        Mockito.when(restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employees", String.class)).thenReturn(objectMapper.writeValueAsString(node));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        JsonNode actualOutput = getResponseFromMvcResult(mvcResult);
        assertTrue(actualOutput.size() != 0);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        JsonNode node = FileUtil.readFromFile("/employee_by_id_dummy_response.json");
        Mockito.when(restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employee/id2", String.class)).thenReturn(objectMapper.writeValueAsString(node));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/id2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JsonNode actualOutput = getResponseFromMvcResult(mvcResult);
        assertTrue(actualOutput.size() != 0);

        Employee employee = objectMapper.convertValue(actualOutput, Employee.class);
        assertEquals("id2", employee.getId());
    }

    @Test
    public void testGetEmployeeByIdNotFound() throws Exception {
        JsonNode node = FileUtil.readFromFile("/employee_by_id_dummy_failed_response.json");
        Mockito.when(restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employee/id3", String.class)).thenReturn(objectMapper.writeValueAsString(node));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/id3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        JsonNode node = FileUtil.readFromFile("/employee_by_id_dummy_response.json");
        Mockito.when(restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employee/id2", String.class)).thenReturn(objectMapper.writeValueAsString(node));

        Mockito.doNothing().when(restTemplate).delete("https://dummy.restapiexample.com/api/v1/delete/id2");

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/id2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("Successfully deleted record!", response);
    }

    @Test
    public void testDeleteEmployeeByIdNotFound() throws Exception {
        JsonNode node = FileUtil.readFromFile("/employee_by_id_dummy_failed_response.json");
        Mockito.when(restTemplate.getForObject("https://dummy.restapiexample.com/api/v1/employee/id3", String.class)).thenReturn(objectMapper.writeValueAsString(node));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/id3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee("id3", "testEmp", 30, 3000000, "");
        Mockito.when(restTemplate.postForObject("https://dummy.restapiexample.com/api/v1/create", objectMapper.createObjectNode(), Employee.class)).thenReturn(employee);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("name", "testEmp");
        inputMap.put("salary", 3400000);
        inputMap.put("age", 28);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputMap))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        JsonNode actualOutput = getResponseFromMvcResult(mvcResult);
        assertTrue(actualOutput.size() != 0);

        Employee newEmployee = objectMapper.convertValue(actualOutput, Employee.class);
        assertEquals("id3", employee.getId());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        Employee employee = new Employee();
        employee.setSalary(5000000);

        Mockito.when(employeeRepository.findTopByOrderBySalaryDesc()).thenReturn(Optional.of(employee));
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/highestSalary"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(5000000, Integer.parseInt(response));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        JsonNode node = FileUtil.readFromFile("/all_employees_dummy_response.json");
        List<Employee> employees = objectMapper.convertValue(node.path("data"), new TypeReference<List<Employee>>() {});

        Mockito.when(employeeRepository.findTop10ByOrderBySalaryDesc()).thenReturn(Optional.of(employees));
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(response.isEmpty());
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        Employee employee = new Employee("id3", "testEmp", 30, 3000000, "");
        List<Employee> employees = Arrays.asList(employee);

        Mockito.when(employeeRepository.searchEmployeesByName("testEmp")).thenReturn(Optional.of(employees));
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/search/testEmp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JsonNode response = getResponseFromMvcResult(mvcResult);
        List<Employee> employeeResp = objectMapper.convertValue(response, new TypeReference<List<Employee>>() {});
        assertEquals("testEmp", employeeResp.get(0).getName());
    }

    private JsonNode getResponseFromMvcResult(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String response = mvcResult.getResponse().getContentAsString();
        return objectMapper.readTree(response);
    }
}
