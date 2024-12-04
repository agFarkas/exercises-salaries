package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeIdValidatorTest {

    private final EmployeeIdValidator employeeIdValidator = new EmployeeIdValidator();

    private final List<Employee> allEmployees = List.of(
            Employee.of(1, "John", "Doe", 105000, null),
            Employee.of(2, "Sara", "Conor", 59000, 1),
            Employee.of(1, "John", "Conor", 70000, 1),
            Employee.of(1, "Han", "Solo", 50000, 2),
            Employee.of(5, "Dick", "Johnson", 52000, 3),
            Employee.of(6, "Marty", "McFly", 40000, 4),
            Employee.of(7, "Emmet", "Brown", 20000, 6),
            Employee.of(8, "Biff", "Tannen", 15000, 7),
            Employee.of(5, "Marilyn", "Monroe", 40000, 5),
            Employee.of(10, "Evlyn", "Roe", 12500, 8),
            Employee.of(11, "Michael", "Jackson", 10500, 10),
            Employee.of(12, "Rubeus", "Hagrid", 40000, 6),
            Employee.of(13, "Indiana", "Jons", 40000, 2),
            Employee.of(14, "Robin", "Hood", 59000, 2)
    );

    @Test
    void idValidationTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> employeeIdValidator.validate(allEmployees)
        );

        assertEquals("Error(s) in validation:\n" +
                "\tThe following Id-s are not unique: 1, 5",
                exception.getMessage());
    }
}
