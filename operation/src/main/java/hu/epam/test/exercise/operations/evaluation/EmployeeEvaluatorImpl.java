package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EmployeeEvaluatorImpl implements EmployeeEvaluator {

    @Override
    public List<ManagerSalaryDifferenceEvaluator> collectManagersEarningOutOfRecommendations(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return collectManagers(allEmployees)
                .stream()
                .map(manager -> makeSalaryDifferenceEvaluator(manager, allEmployeesCopy))
                .filter(ManagerSalaryDifferenceEvaluator::isOutOfRecommendations)
                .toList();
    }

    @Override
    public List<EmployeeReportingLineEvaluator> collectEmployeesWithTooLongReportingLines(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return allEmployees.stream()
                .map(employee -> buildReportingLineOfEmployee(employee, allEmployeesCopy))
                .filter(EmployeeReportingLineEvaluator::isLongerThanRecommended)
                .toList();
    }

    private ManagerSalaryDifferenceEvaluator makeSalaryDifferenceEvaluator(Employee manager, List<Employee> allEmployees) {
        return new ManagerSalaryDifferenceEvaluator(manager, allEmployees);
    }

    private List<Employee> collectManagers(List<Employee> allEmployees) {
        var allEmployeesCopy = new LinkedList<>(allEmployees);

        return allEmployees.stream()
                .filter(employee -> isAssignedAsManager(employee, allEmployeesCopy))
                .toList();
    }

    private boolean isAssignedAsManager(Employee employee, LinkedList<Employee> allEmployees) {
        return allEmployees.stream()
                .anyMatch(emp -> Objects.equals(employee.getId(), emp.getManagerId()));
    }

    private EmployeeReportingLineEvaluator buildReportingLineOfEmployee(Employee employee, List<Employee> allEmployees) {
        return new EmployeeReportingLineEvaluator(employee, allEmployees);
    }

}
