package com.example.employee_department.service;


import com.example.employee_department.Model.Department;
import com.example.employee_department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmployeeReportService {

    private final DepartmentRepository departmentRepository;

    public byte[] generateReport() throws JRException {
        try {
            // Compile main report
            InputStream mainReportStream = getClass().getResourceAsStream("/reports/employees_by_department.jrxml");
            if (mainReportStream == null) {
                throw new RuntimeException("Main report template not found");
            }
            JasperReport mainReport = JasperCompileManager.compileReport(mainReportStream);

            // Compile subreport
            InputStream subreportStream = getClass().getResourceAsStream("/reports/employees_subreport.jrxml");
            if (subreportStream == null) {
                throw new RuntimeException("Subreport template not found");
            }
            JasperReport subreport = JasperCompileManager.compileReport(subreportStream);

            // Parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Employees by Department");
            parameters.put("GENERATED_DATE", new Date());
            parameters.put("SUBREPORT", subreport); // Pass the compiled subreport directly

            // Data
            List<Department> departments = departmentRepository.findAll();
            if (departments.isEmpty()) {
                throw new RuntimeException("No departments found to generate report");
            }

            // Create data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(departments);

            // Generate and export report
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace(); // This will help debug any issues
            throw new JRException("Failed to generate report", e);
        }
    }

}
