package hu.epam.test.exercise.operations.validation;

import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.common.validation.AbstractValidator;
import hu.epam.test.exercise.model.Employee;

import java.util.List;

/**
 * The main logical validator that contains the specific logical validators to make the validations in their separate aspects
 * on the list of {@link Employee}s.
 */
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

    /**
     * Validates a list of {@link Employee}s by the specific logical validators. If one of them finds any kind of
     * invalidity, escalates a {@link hu.epam.test.exercise.common.exception.ValidationException}.
     *
     * @param employees The list to validate.
     */
    @Override
    public void validate(List<Employee> employees) {
        employeeIdValidator.validate(employees);
        employeeCEOValidator.validate(employees);
        employeeReportingLineValidator.validate(employees);
    }
}
