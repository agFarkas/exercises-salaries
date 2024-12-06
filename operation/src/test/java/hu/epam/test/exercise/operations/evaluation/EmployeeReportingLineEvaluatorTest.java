package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeReportingLineEvaluatorTest {

    private final List<Employee> allEmployees = List.of(
            Employee.of(1, "John", "Doe", 105000, null),
            Employee.of(2, "Sara", "Conor", 59000, 1),
            Employee.of(3, "John", "Conor", 70000, 1),
            Employee.of(4, "Han", "Solo", 50000, 2),
            Employee.of(5, "Dick", "Johnson", 52000, 3),
            Employee.of(6, "Marty", "McFly", 40000, 4),
            Employee.of(7, "Emmet", "Brown", 20000, 6),
            Employee.of(8, "Biff", "Tannen", 15000, 7)
    );

    @Test
    void employeeReportingLineInRangeTest() {
        var employeeReportingLineEvaluator = buildEvaluator(Employee.of(6, "Marty", "McFly", 40000, 4));

        assertEquals(3, employeeReportingLineEvaluator.getReportingLine());
        assertFalse(employeeReportingLineEvaluator.isLongerThanRecommended());
        assertEquals(0, employeeReportingLineEvaluator.getDifference());
    }

    @Test
    void employeeReportingLineLongerTest() {
        var employeeReportingLineEvaluator = buildEvaluator(
                Employee.of(8, "Biff", "Tannen", 15000, 7)
        );

        assertEquals(5, employeeReportingLineEvaluator.getReportingLine());
        assertTrue(employeeReportingLineEvaluator.isLongerThanRecommended());
        assertEquals(1, employeeReportingLineEvaluator.getDifference());
    }

    private EmployeeReportingLineEvaluator buildEvaluator(Employee employee) {
        return new EmployeeReportingLineEvaluator(employee, allEmployees);
    }

}
