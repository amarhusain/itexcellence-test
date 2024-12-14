package com.example.employee_department.controller;


import com.example.employee_department.dto.EmployeeDTO;
import com.example.employee_department.service.EmployeeReportService;
import com.example.employee_department.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeReportService employeeReportService;

    @GetMapping()
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{departmentId}")
    public List<EmployeeDTO> getEmployeesByDepartment(@PathVariable String departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId);
    }

    @PostMapping("add/{departmentId}")
    public EmployeeDTO addEmployee(@PathVariable String departmentId, @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.addEmployee(departmentId, employeeDTO);
    }

    @DeleteMapping("/{departmentId}/{employeeId}")
    public void deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @GetMapping("/emp/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);

    }


    @GetMapping(value = "/reports", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateEmployeeReport() {
        try {
            byte[] reportBytes = employeeReportService.generateReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "employees-by-department.pdf");

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (JRException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable String id,
            @RequestBody EmployeeDTO employeeDetails) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
