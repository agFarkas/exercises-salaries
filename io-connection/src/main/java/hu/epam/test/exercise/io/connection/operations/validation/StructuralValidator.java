package hu.epam.test.exercise.io.connection.operations.validation;



import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.common.validation.AbstractValidator;

import java.util.List;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;
import static hu.epam.test.exercise.common.util.EmployeeUtil.getHeaderLine;


public class StructuralValidator extends AbstractValidator<List<String[]>> {

    private final AbstractListValidator<String> headerValidator;

    private final AbstractListValidator<String[]> employeeStructuralValidator;

    public StructuralValidator(
            AbstractListValidator<String> headerValidator,
            AbstractListValidator<String[]> employeeStructuralValidator
    ) {
        this.headerValidator = headerValidator;
        this.employeeStructuralValidator = employeeStructuralValidator;
    }

    @Override
    public void validate(List<String[]> lines) {
        headerValidator.validate(getHeaderLine(lines));
        employeeStructuralValidator.validate(getEmployeeLines(lines));
    }

}
