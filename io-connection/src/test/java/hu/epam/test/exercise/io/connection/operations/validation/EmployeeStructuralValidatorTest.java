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

        assertEquals("Error(s) in validation:\n" +
                        "\tError(s) in validation of employee line 1\n" +
                        "\t\tfirstName is blank, but must be provided.\n" +
                        "\tError(s) in validation of employee line 2\n" +
                        "\t\tlastName is blank, but must be provided.\n" +
                        "\t\tsalary is blank, but must be provided.",
                exception.getMessage());
    }

    @Test
    void invalideEmployeeByNonNumbericValueTest() {
        var tableLines = readTableLines("employees-invalid-by-non-numeric-data.csv");

        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines))
        );

        assertEquals("Error(s) in validation:\n" +
                "\tError(s) in validation of employee line 1\n" +
                "\t\tsalary is expected to be a number, but is not.\n" +
                "\tError(s) in validation of employee line 5\n" +
                "\t\tId is expected to be a number, but is not.", exception.getMessage());
    }

    @Test
    void invalideEmployeeByIncorrectFieldNumberValueTest() {
        var tableLines = readTableLines("employees-invalid-by-incorrect-field-number.csv");

        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines))
        );

        assertEquals("Error(s) in validation:\n" +
                "\tError(s) in validation of employee line 3\n" +
                "\t\tLine 3 should have 5 separated fields, but it has actually 3.\n" +
                "\tError(s) in validation of employee line 4\n" +
                "\t\tLine 4 should have 5 separated fields, but it has actually 6.", exception.getMessage());
    }
}
