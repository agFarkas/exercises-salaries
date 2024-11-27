package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HeaderValidatorTest extends TestParent {

    private final HeaderValidator headerValidator = new HeaderValidator();

    @Test
    void validHeaderTest() {
        var tableLines = readTableLines("employees-valid.csv");
        assertDoesNotThrow(() -> headerValidator.validate(EmployeeUtil.getHeaderLine(tableLines)));
    }

    @Test
    void invalidHeaderByTypoTest() {
        var tableLines = readTableLines("employees-invalid-header-by-typo.csv");
        assertValidationErrorMessage(tableLines);
    }

    @Test
    void invalidHeaderByFieldNumberTest() {
        var tableLines = readTableLines("employees-invalid-header-by-typo.csv");
        assertValidationErrorMessage(tableLines);
    }

    private void assertValidationErrorMessage(List<String[]> tableLines) {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> headerValidator.validate(EmployeeUtil.getHeaderLine(tableLines))
        );

        assertEquals(
                "Error(s) in validation:\n" +
                "\tThe file headline must be exactly as follows: Id, firstName, lastName, salary, managerId",
                exception.getMessage()
        );
    }
}
