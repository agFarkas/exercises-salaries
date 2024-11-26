package hu.epam.test.exercise.io.connection.operations.validation;

import org.springframework.stereotype.Component;

import java.util.List;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;
import static hu.epam.test.exercise.common.util.EmployeeUtil.getHeaderLine;

@Component
public class StructuralValidator {

    private final HeaderValidator headerValidator;

    private final EmployeeStructuralValidator employeeStructuralValidator;

    public StructuralValidator(
            HeaderValidator headerValidator,
            EmployeeStructuralValidator employeeStructuralValidator
    ) {
        this.headerValidator = headerValidator;
        this.employeeStructuralValidator = employeeStructuralValidator;
    }

    public void validate(List<String[]> lines) {
        headerValidator.validate(getHeaderLine(lines));
        employeeStructuralValidator.validate(getEmployeeLines(lines));
    }

}
