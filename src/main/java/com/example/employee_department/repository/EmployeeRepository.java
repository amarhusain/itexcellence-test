package com.example.employee_department.repository;


import com.example.employee_department.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
   // Check if an employee exists with the given email
   boolean existsByEmail(String email);
   List<Employee> findByDepartmentId(String departmentId);
   // Optional: Find employee by email
   Optional<Employee> findByEmail(String email);
}
