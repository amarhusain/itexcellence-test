package com.example.employee_department.repository;


import com.example.employee_department.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
   List<Employee> findByDepartmentId(String departmentId);
}
