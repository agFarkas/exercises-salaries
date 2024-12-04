package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.util.List;

public interface EmployeeEvaluator {

    List<ManagerSalaryDifferenceEvaluator> collectManagersEarningOutOfRecommendations(List<Employee> allEmployees);

    List<EmployeeReportingLineEvaluator> collectEmployeesWithTooLongReportingLines(List<Employee> allEmployees);
}
