package com.example.employee_department.service;

import com.example.employee_department.Model.Department;
import com.example.employee_department.Model.Employee;
import com.example.employee_department.dto.DepartmentDTO;
import com.example.employee_department.dto.EmployeeDTO;
import com.example.employee_department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Department> saveAllDepartments(List<Department> departments) {
        return departmentRepository.saveAll(departments); // Save all departments
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        dto.setEmployees(department.getEmployees().stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        return dto;
    }


}


