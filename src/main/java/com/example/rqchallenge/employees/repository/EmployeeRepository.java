package com.example.rqchallenge.employees.repository;

import com.example.rqchallenge.employees.payload.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findTopByOrderBySalaryDesc();

    Optional<List<Employee>> findTop10ByOrderBySalaryDesc();

    @Query("select employee from EmployeeEntity employee " +
            "where employee.name like concat('%', :searchQuery, '%')")
    Optional<List<Employee>> searchEmployeesByName(String searchQuery);

    // Native query option
    /*@Query(value = "select * from employee e" +
            "where e.name like concat('%', :searchQuery, '%')", nativeQuery = true)
    Optional<List<EmployeeEntity>> searchEmployeesByName(String searchQuery);*/
}
