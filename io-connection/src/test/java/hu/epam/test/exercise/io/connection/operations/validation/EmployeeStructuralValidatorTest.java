package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmployeeStructuralValidatorTest extends TestParent {

    private final EmployeeStructuralValidator employeeStructuralValidator = new EmployeeStructuralValidator();

    @Test
    void validEmployeesTest() {
        var tableLines = readTableLines("employees-valid.csv");
        assertThatCode(() -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines)))
                .doesNotThrowAnyException();
    }

    @Test
    void invalideEmployeeByMissingMandatoryValueTest() {
        var tableLines = readTableLines("employees-invalid-by-missing-mandatory-value.csv");

        assertThatThrownBy(() -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines)))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tError(s) in validation of employee line 1\n" +
                                "\t\tfirstName is blank, but must be provided.\n" +
                                "\tError(s) in validation of employee line 2\n" +
                                "\t\tlastName is blank, but must be provided.\n" +
                                "\t\tsalary is blank, but must be provided."
                );
    }

    @Test
    void invalideEmployeeByNonNumbericValueTest() {
        var tableLines = readTableLines("employees-invalid-by-non-numeric-data.csv");

        assertThatThrownBy(() -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines)))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tError(s) in validation of employee line 1\n" +
                                "\t\tsalary is expected to be a number, but is not.\n" +
                                "\tError(s) in validation of employee line 5\n" +
                                "\t\tId is expected to be a number, but is not."
                );
    }

    @Test
    void invalideEmployeeByIncorrectFieldNumberValueTest() {
        var tableLines = readTableLines("employees-invalid-by-incorrect-field-number.csv");

        assertThatThrownBy(() -> employeeStructuralValidator.validate(EmployeeUtil.getEmployeeLines(tableLines)))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tError(s) in validation of employee line 3\n" +
                                "\t\tLine 3 should have 5 separated fields, but it has actually 3.\n" +
                                "\tError(s) in validation of employee line 4\n" +
                                "\t\tLine 4 should have 5 separated fields, but it has actually 6."
                );
    }
}
