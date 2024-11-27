package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeCEOValidatorTest {

    private final EmployeeCEOValidator employeeCEOValidator = new EmployeeCEOValidator();

    @Test
    void validCEOEmployeeTest() {
        assertDoesNotThrow(() -> employeeCEOValidator.validate(List.of(
                Employee.of(1, "John", "Doe", 72000, null),
                Employee.of(2, "Sara", "Conor", 72000, 1),
                Employee.of(3, "Han", "Solo", 72000, 1)
        )));
    }

    @Test
    void invalidByMultipleCEOEmployeesTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeCEOValidator.validate(List.of(
                        Employee.of(1, "John", "Doe", 72000, null),
                        Employee.of(2, "Sara", "Conor", 72000, null),
                        Employee.of(3, "Han", "Solo", 72000, 1)
                )));

        assertEquals("Error(s) in validation:\n" +
                "\tThere must be exactly one CEO. Multiple CEOs were actually provided as follows by ID: [1, 2]", exception.getMessage());
    }

    @Test
    void invalidByNoCEOEmployeeTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeCEOValidator.validate(List.of(
                        Employee.of(1, "John", "Doe", 72000, 2),
                        Employee.of(2, "Sara", "Conor", 72000, 3),
                        Employee.of(3, "Han", "Solo", 72000, 1)
                )));

        assertEquals("Error(s) in validation:\n" +
                "\tThere must be exactly one CEO. No CEO was actually provided.", exception.getMessage());
    }
}
