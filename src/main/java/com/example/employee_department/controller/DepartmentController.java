package com.example.employee_department.controller;


import com.example.employee_department.Model.Department;
import com.example.employee_department.dto.DepartmentDTO;
import com.example.employee_department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping()
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping()
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.createDepartment(departmentDTO);
    }


}

