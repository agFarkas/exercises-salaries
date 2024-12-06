package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.common.validation.AbstractValidator;
import hu.epam.test.exercise.model.Employee;

import java.util.List;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;
import static hu.epam.test.exercise.common.util.EmployeeUtil.getHeaderLine;

/**
 * The main structural validator that contains the specific structural validators to make the validations in their
 * separate aspects on the list of {@link Employee}s.
 */
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

    /**
     * Validates the lines of the entire data set separately for its header and its employee lines.
     * If one of them finds any kind of
     * invalidity, escalates a {@link hu.epam.test.exercise.common.exception.ValidationException}.
     *
     * @param lines The raw lines of the dataset to validate.
     */
    @Override
    public void validate(List<String[]> lines) {
        headerValidator.validate(getHeaderLine(lines));
        employeeStructuralValidator.validate(getEmployeeLines(lines));
    }

}
