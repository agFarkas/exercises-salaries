package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.model.Employee;


import java.util.List;


public class LogicalValidator {

    private final EmployeeIdValidator employeeIdValidator;

    private final EmployeeCEOValidator employeeCEOValidator;

    private final EmployeeReportingLineValidator employeeReportingLineValidator;

    public LogicalValidator(
            EmployeeIdValidator employeeIdValidator,
            EmployeeCEOValidator employeeCEOValidator,
            EmployeeReportingLineValidator employeeReportingLineValidator
    ) {
        this.employeeIdValidator = employeeIdValidator;
        this.employeeCEOValidator = employeeCEOValidator;
        this.employeeReportingLineValidator = employeeReportingLineValidator;
    }

    public void validate(List<Employee> employees) {
        employeeIdValidator.validate(employees);
        employeeCEOValidator.validate(employees);
        employeeReportingLineValidator.validate(employees);
    }
}
