package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeReportingLineValidatorTest {

    private final EmployeeReportingLineValidator employeeReportingLineValidator = new EmployeeReportingLineValidator();

    @Test
    void validReportingLineTest() {
        assertDoesNotThrow(() -> employeeReportingLineValidator.validate(List.of(
                Employee.of(1, "John", "Doe", 72000, null),
                Employee.of(2, "Sara", "Conor", 60000, 1),
                Employee.of(3, "Han", "Solo", 59000, 1)
        )));
    }

    @Test
    void invalidReportingLineByManagerNotFoundTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeReportingLineValidator.validate(List.of(
                        Employee.of(1, "John", "Doe", 72000, null),
                        Employee.of(2, "Sara", "Conor", 72000, 1),
                        Employee.of(3, "Han", "Solo", 72000, 100)
                ))
        );

        assertEquals("Error(s) in validation:\n" +
                        "\tManager 100 not found by Id.",
                exception.getMessage());
    }

    @Test
    void invalidReportingLineBySelfManagerTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeReportingLineValidator.validate(List.of(
                        Employee.of(1, "John", "Doe", 72000, null),
                        Employee.of(2, "Sara", "Conor", 72000, 2),
                        Employee.of(3, "Han", "Solo", 72000, 1)
                ))
        );
        assertEquals("Error(s) in validation:\n" +
                        "\tEmployee 2 has assingned to themself as manager.",
                exception.getMessage()
        );
    }

    @Test
    void invalidReportingLineByCircularLineTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeReportingLineValidator.validate(List.of(
                        Employee.of(1, "John", "Doe", 72000, null),
                        Employee.of(2, "Sara", "Conor", 72000, 5),
                        Employee.of(3, "Han", "Solo", 72000, 2),
                        Employee.of(4, "Han", "Solo", 72000, 3),
                        Employee.of(5, "Han", "Solo", 72000, 4)
                ))
        );

        assertEquals(
                "Error(s) in validation:\n" +
                        "\tCircular reporting line by ids: 2 -> 5 -> 4 -> 3 -> 2\n" +
                        "\tCircular reporting line by ids: 3 -> 2 -> 5 -> 4 -> 3\n" +
                        "\tCircular reporting line by ids: 4 -> 3 -> 2 -> 5 -> 4\n" +
                        "\tCircular reporting line by ids: 5 -> 4 -> 3 -> 2 -> 5"
                ,exception.getMessage()
        );
    }
}
