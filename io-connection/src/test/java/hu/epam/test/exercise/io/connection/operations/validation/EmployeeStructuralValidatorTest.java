package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeStructuralValidatorTest extends TestParent {

    private final EmployeeStructuralValidator employeeStructuralValidator = new EmployeeStructuralValidator();

    @Test
    void validEmployeesTest() {
        var tableLines = readTableLines("employees.csv");
        assertDoesNotThrow(() -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines)));
    }

    @Test
    void invalideEmployeeByMissingMandatoryValueTest() {
        var tableLines = readTableLines("employees-invalid-by-missing-mandatory-value.csv");

        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines))
        );

        assertEquals("""
                        Error(s) in validation:
                        \tError(s) in validation of employee line 1
                        \t\tfirstName is blank, but must be provided.
                        \tError(s) in validation of employee line 2
                        \t\tlastName is blank, but must be provided.
                        \t\tsalary is blank, but must be provided.""",
                exception.getMessage());
    }

    @Test
    void invalideEmployeeByNonNumbericValueTest() {
        var tableLines = readTableLines("employees-invalid-by-non-numeric-data.csv");

        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines))
        );

        assertEquals("""
                Error(s) in validation:
                \tError(s) in validation of employee line 1
                \t\tsalary is expected to be a number, but is not.
                \tError(s) in validation of employee line 5
                \t\tId is expected to be a number, but is not.""", exception.getMessage());
    }

    @Test
    void invalideEmployeeByIncorrectFieldNumberValueTest() {
        var tableLines = readTableLines("employees-invalid-by-incorrect-field-number.csv");

        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines))
        );

        assertEquals("""
                Error(s) in validation:
                \tError(s) in validation of employee line 3
                \t\tLine 3 should have 5 separated fields, but it has actually 3.
                \tError(s) in validation of employee line 4
                \t\tLine 4 should have 5 separated fields, but it has actually 6.""", exception.getMessage());
    }
}
