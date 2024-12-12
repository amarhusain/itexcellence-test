package com.example.employee_department.service;

import com.example.employee_department.Model.Department;
import com.example.employee_department.Model.Employee;
import com.example.employee_department.dto.DepartmentDTO;
import com.example.employee_department.dto.EmployeeDTO;
import com.example.employee_department.exceptions.DuplicateResourceException;
import com.example.employee_department.exceptions.ValidationException;
import com.example.employee_department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // Validate department data
        validateDepartment(departmentDTO);

        // Check for duplicate department ID
        if (departmentRepository.existsById(departmentDTO.getId())) {
            throw new DuplicateResourceException(
                    "Department already exists with id: " + departmentDTO.getId());
        }

        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        department.setLocation(departmentDTO.getLocation());

        return convertToDTO(departmentRepository.save(department));
    }

    private void validateDepartment(DepartmentDTO departmentDTO) {
        Map<String, String> errors = new HashMap<>();

        if (departmentDTO.getId() == null || departmentDTO.getId().trim().isEmpty()) {
            errors.put("id", "Department ID is required");
        }
        if (departmentDTO.getName() == null || departmentDTO.getName().trim().isEmpty()) {
            errors.put("name", "Department name is required");
        }
        if (departmentDTO.getLocation() == null || departmentDTO.getLocation().trim().isEmpty()) {
            errors.put("location", "Department location is required");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        if(department.getEmployees() != null){
            dto.setEmployees(department.getEmployees().stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList()));
        }
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


