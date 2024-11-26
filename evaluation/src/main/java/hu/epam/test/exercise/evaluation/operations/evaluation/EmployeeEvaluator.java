package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EmployeeEvaluator {

    public List<ManagerSalaryDifferenceEvaluator> collectManagersEarningOutOfRecommendations(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return collectManagers(allEmployees)
                .stream()
                .map(manager -> makeSalaryDifferenceCalculator(manager, allEmployeesCopy))
                .filter(ManagerSalaryDifferenceEvaluator::isOutOfRecommendations)
                .toList();
    }

    public List<EmployeeReportingLineEvaluator> collectEmployeesWithTooLongReportingLines(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return allEmployees.stream()
                .map(emp -> buildReportingLineOfEmployee(emp, allEmployeesCopy))
                .filter(EmployeeReportingLineEvaluator::isLongerThanRecommended)
                .toList();

    }

    private ManagerSalaryDifferenceEvaluator makeSalaryDifferenceCalculator(Employee manager, List<Employee> allEmployees) {
        var subordinates = obtainSubordinates(manager, allEmployees);
        var average = calculateAverageSalary(subordinates);

        return new ManagerSalaryDifferenceEvaluator(manager, new BigDecimal(average));
    }

    private List<Employee> obtainSubordinates(Employee manager, List<Employee> allEmployees) {
        return allEmployees.stream()
                .filter(employee -> isSubordinateOf(manager, employee))
                .toList();
    }

    private static double calculateAverageSalary(List<Employee> subordinates) {
        return subordinates.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    private boolean isSubordinateOf(Employee manager, Employee employee) {
        return Objects.equals(employee.getManagerId(), manager.getId());
    }

    private List<Employee> collectManagers(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return allEmployees.stream()
                .filter(emp -> isAssignedAsManager(emp, allEmployeesCopy))
                .toList();
    }

    private boolean isAssignedAsManager(Employee employee, LinkedList<Employee> allEmployees) {
        return allEmployees.stream()
                .anyMatch(emp -> Objects.equals(employee.getId(), emp.getManagerId()));
    }

    private EmployeeReportingLineEvaluator buildReportingLineOfEmployee(Employee employee, List<Employee> allEmployees) {
        var reportingLine = new LinkedHashSet<Employee>();
        var currentEmployee = new AtomicReference<>(employee);

        do {
            allEmployees.stream()
                    .filter(emp -> Objects.equals(emp.getId(), currentEmployee.get().getManagerId()))
                    .findAny()
                    .ifPresent(currentEmployee::set);

            reportingLine.add(currentEmployee.get());

        } while (!currentEmployee.get().isCEO());

        return new EmployeeReportingLineEvaluator(employee, reportingLine);
    }
}
