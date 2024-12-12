package com.example.employee_department.service;


import com.example.employee_department.Model.Department;
import com.example.employee_department.Model.Employee;
import com.example.employee_department.dto.EmployeeDTO;
import com.example.employee_department.exceptions.DuplicateResourceException;
import com.example.employee_department.exceptions.ResourceNotFoundException;
import com.example.employee_department.exceptions.ValidationException;
import com.example.employee_department.repository.DepartmentRepository;
import com.example.employee_department.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;


    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesByDepartment(String departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + departmentId));

        return department.getEmployees().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public EmployeeDTO addEmployee(String departmentId, EmployeeDTO employeeDTO) {
        // Validate employee data
        validateEmployee(employeeDTO);

        // Check for duplicate email
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateResourceException(
                    "Employee already exists with email: " + employeeDTO.getEmail());
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + departmentId));


        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPosition(employeeDTO.getPosition());
        employee.setSalary(employeeDTO.getSalary());
        employee.setDepartment(department);

        return convertToDTO(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    "Employee not found with id: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }


    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .build();
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        Map<String, String> errors = new HashMap<>();

        if (employeeDTO.getName() == null || employeeDTO.getName().trim().isEmpty()) {
            errors.put("name", "Name is required");
        }
        if (employeeDTO.getEmail() == null || !employeeDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Valid email is required");
        }
        if (employeeDTO.getPosition() == null || employeeDTO.getPosition().trim().isEmpty()) {
            errors.put("position", "Position is required");
        }
        if (employeeDTO.getSalary() <= 0) {
            errors.put("salary", "Salary must be greater than 0");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
