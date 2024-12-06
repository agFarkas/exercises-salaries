package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerSalaryDifferenceEvaluatorTest {

    private final List<Employee> allEmployees = List.of(
            Employee.of(1, "John", "Doe", 105000, null),
            Employee.of(2, "Sara", "Conor", 59000, 1),
            Employee.of(3, "John", "Conor", 70000, 1),
            Employee.of(4, "Han", "Solo", 50000, 2),
            Employee.of(5, "Dick", "Johnson", 52000, 3),
            Employee.of(6, "Marty", "McFly", 40000, 4),
            Employee.of(7, "Emmet", "Brown", 20000, 6),
            Employee.of(8, "Biff", "Tannen", 15000, 7),
            Employee.of(9, "Marilyn", "Monroe", 40000, 5),
            Employee.of(10, "Evlyn", "Roe", 12500, 8),
            Employee.of(11, "Michael", "Jackson", 10500, 10),
            Employee.of(12, "Rubeus", "Hagrid", 40000, 6),
            Employee.of(13, "Indiana", "Jons", 40000, 2),
            Employee.of(14, "Robin", "Hood", 59000, 2)
    );

    @Test
    void managerSalaryInRecommendedRangeTest() {
        var managerSalaryDifferenceEvaluator = buildEvaluator(Employee.of(7, "Emmet", "Brown", 20000, 6));

        assertFalse(managerSalaryDifferenceEvaluator.isLessThanMinimum());
        assertFalse(managerSalaryDifferenceEvaluator.isMoreThanMaximum());
        assertEquals(
                15000,
                managerSalaryDifferenceEvaluator.getAverageSalaryOfSubordinates()
                        .doubleValue()
        );
        assertEquals(
                BigDecimal.ZERO,
                managerSalaryDifferenceEvaluator.calculateDifferenceFromRecommendation()
        );
    }

    @Test
    void managerSalaryLessThanMinimumTest() {
        var managerSalaryDifferenceEvaluator = buildEvaluator(
                Employee.of(2, "Sara", "Conor", 59000, 1)
        );

        assertTrue(managerSalaryDifferenceEvaluator.isLessThanMinimum());
        assertEquals(
                new BigDecimal("49666.67"),
                managerSalaryDifferenceEvaluator.getAverageSalaryOfSubordinates()
        );
        assertEquals(new BigDecimal("-600.004"),
                managerSalaryDifferenceEvaluator.calculateDifferenceFromRecommendation()
        );
    }

    @Test
    void managerSalaryMoreThanMaximumTest() {
        var managerSalaryDifferenceEvaluator = buildEvaluator(
                Employee.of(1, "John", "Doe", 105000, null)
        );

        assertTrue(managerSalaryDifferenceEvaluator.isMoreThanMaximum());
        assertEquals(
                64500,
                managerSalaryDifferenceEvaluator.getAverageSalaryOfSubordinates()
                        .doubleValue()
        );
        assertEquals(8250,
                managerSalaryDifferenceEvaluator.calculateDifferenceFromRecommendation()
                        .doubleValue()
        );
    }

    private ManagerSalaryDifferenceEvaluator buildEvaluator(Employee employee) {
        return new ManagerSalaryDifferenceEvaluator(
                employee,
                allEmployees
        );
    }

}
