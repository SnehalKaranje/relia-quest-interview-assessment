package com.example.rqchallenge.employees.payload;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee {
    @Id
    private String id;
    private String name;
    private int age;
    private int salary;
    private String profileImage;

    public Employee(String id, String name, int age, int salary, String profileImage) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.profileImage = profileImage;
    }
}
