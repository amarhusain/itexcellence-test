package com.example.employee_department.repository;


import com.example.employee_department.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    // Check if an employee exists with the given id
    boolean existsById(String departmentId);
}

