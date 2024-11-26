package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeEvaluatorTest {

    private EmployeeEvaluator employeeEvaluator = new EmployeeEvaluator();

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
    void collectEmployeesWithTooLongReportingLinesTest() {
        var concernedEmployees = employeeEvaluator.collectEmployeesWithTooLongReportingLines(allEmployees);
        assertThat(concernedEmployees).hasSize(3);
        var employeeIds = concernedEmployees.stream()
                .map(EmployeeReportingLineEvaluator::getEmployee)
                .map(Employee::getId)
                .toList();

        assertThat(employeeIds).containsExactlyInAnyOrder(8, 10, 11);
    }

    @Test
    void collectManagersEarningOutOfRecommendationsTest() {
        var concernedManagers = employeeEvaluator.collectManagersEarningOutOfRecommendations(allEmployees);
        var managerIds = concernedManagers.stream()
                .map(ManagerSalaryDifferenceEvaluator::getManager)
                .map(Employee::getId)
                .toList();

        assertThat(managerIds).containsExactlyInAnyOrder(1, 2, 10);
    }
}
