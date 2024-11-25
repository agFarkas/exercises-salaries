package hu.epam.exercise.operations.validation;

import hu.epam.exercise.model.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogicalValidator {

    private final EmployeeCEOValidator employeeCEOValidator;

    private final EmployeeReportingLineValidator employeeReportingLineValidator;

    public LogicalValidator(EmployeeCEOValidator employeeCEOValidator, EmployeeReportingLineValidator employeeReportingLineValidator) {
        this.employeeCEOValidator = employeeCEOValidator;
        this.employeeReportingLineValidator = employeeReportingLineValidator;
    }

    public void validate(List<Employee> employees) {
        employeeCEOValidator.validate(employees);
        employeeReportingLineValidator.validate(employees);
    }
}
