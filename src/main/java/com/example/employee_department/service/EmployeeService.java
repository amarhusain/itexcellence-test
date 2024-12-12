package com.example.employee_department.service;


import com.example.employee_department.Model.Department;
import com.example.employee_department.Model.Employee;
import com.example.employee_department.dto.EmployeeDTO;
import com.example.employee_department.repository.DepartmentRepository;
import com.example.employee_department.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public EmployeeDTO addEmployee(String departmentId, EmployeeDTO employeeDTO) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

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

}
