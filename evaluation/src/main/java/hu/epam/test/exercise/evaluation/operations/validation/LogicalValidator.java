package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.common.validation.AbstractValidator;
import hu.epam.test.exercise.model.Employee;


import java.util.List;


public class LogicalValidator extends AbstractValidator<List<Employee>> {

    private final AbstractListValidator<Employee> employeeIdValidator;

    private final AbstractListValidator<Employee> employeeCEOValidator;

    private final AbstractListValidator<Employee> employeeReportingLineValidator;

    public LogicalValidator(
            AbstractListValidator<Employee> employeeIdValidator,
            AbstractListValidator<Employee> employeeCEOValidator,
            AbstractListValidator<Employee> employeeReportingLineValidator
    ) {
        this.employeeIdValidator = employeeIdValidator;
        this.employeeCEOValidator = employeeCEOValidator;
        this.employeeReportingLineValidator = employeeReportingLineValidator;
    }

    @Override
    public void validate(List<Employee> employees) {
        employeeIdValidator.validate(employees);
        employeeCEOValidator.validate(employees);
        employeeReportingLineValidator.validate(employees);
    }
}
