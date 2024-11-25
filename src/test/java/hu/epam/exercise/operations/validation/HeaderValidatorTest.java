package hu.epam.exercise.operations.validation;

import hu.epam.exercise.TestParent;
import hu.epam.exercise.exception.ValidationException;
import hu.epam.exercise.util.EmployeeUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HeaderValidatorTest extends TestParent {

    private final HeaderValidator headerValidator = new HeaderValidator();

    @Test
    void validHeaderTest() {
        var tableLines = readTableLines("employees-valid.csv");
        assertThatCode(() -> headerValidator.validate(EmployeeUtil.getHeaderLine(tableLines)))
                .doesNotThrowAnyException();
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
        assertThatThrownBy(() -> headerValidator.validate(EmployeeUtil.getHeaderLine(tableLines)))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tThe file headline must be exactly as follows: Id, firstName, lastName, salary, managerId"
                );
    }
}
