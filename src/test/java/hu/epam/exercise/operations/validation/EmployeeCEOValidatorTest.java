package hu.epam.exercise.operations.validation;

import hu.epam.exercise.TestParent;
import hu.epam.exercise.exception.ValidationException;
import hu.epam.exercise.model.Employee;
import hu.epam.exercise.util.EmployeeUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmployeeCEOValidatorTest extends TestParent {

    private final EmployeeCEOValidator employeeCEOValidator = new EmployeeCEOValidator();

    @Test
    void validCEOEmployeeTest() {
        assertThatCode(() -> employeeCEOValidator.validate(List.of(
                Employee.of(1, "John", "Doe", 72000, null),
                Employee.of(2, "Sara", "Conor", 72000, 1),
                Employee.of(3, "Han", "Solo", 72000, 1)
        ))).doesNotThrowAnyException();
    }

    @Test
    void invalidByMultipleCEOEmployeesTest() {
        assertThatThrownBy(() -> employeeCEOValidator.validate(List.of(
                Employee.of(1, "John", "Doe", 72000, null),
                Employee.of(2, "Sara", "Conor", 72000, null),
                Employee.of(3, "Han", "Solo", 72000, 1)
        ))).isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tThere must be exactly one CEO. Multiple CEOs were actually provided as follows by ID: [1, 2]"
                );
    }

    @Test
    void invalidByNoCEOEmployeeTest() {
        assertThatThrownBy(() -> employeeCEOValidator.validate(List.of(
                Employee.of(1, "John", "Doe", 72000, 2),
                Employee.of(2, "Sara", "Conor", 72000, 3),
                Employee.of(3, "Han", "Solo", 72000, 1)
        ))).isExactlyInstanceOf(ValidationException.class)
                .hasMessage(
                        "Error(s) in validation:\n" +
                                "\tThere must be exactly one CEO. No CEO was actually provided."
                );
    }
}
