package hu.epam.test.exercise.service;

import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeEvaluator;
import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeReportingLineEvaluator;
import hu.epam.test.exercise.evaluation.operations.evaluation.ManagerSalaryDifferenceEvaluator;
import hu.epam.test.exercise.model.Employee;


import java.util.List;
import java.util.function.Predicate;


public class ReportService {

    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_UNDERPAID = "Managers with less salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_OVERPAID = "Managers with more salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_EMPLOYEES_WITH_TOO_LONG_REPORTING_LINE = "Employees with reporting lines longer than %s:";

    private static final String REPORT_PATTERN__MANAGER_PAID = "\t%s %s (%s) - %s (%s%% more than the average salary of their subordinates: %s)";
    private static final String REPORT_PATTERN__EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE = "\t%s %s (%s) - %s long reporting line (%s more)";

    private final EmployeeEvaluator employeeEvaluator;

    public ReportService(EmployeeEvaluator employeeEvaluator) {
        this.employeeEvaluator = employeeEvaluator;
    }

    public void report(List<Employee> employees) {
        reportManagersWithSalariesOutOfRecommendations(employees);
        reportEmployeesWithTooLongReportingLine(employees);
    }

    private void reportManagersWithSalariesOutOfRecommendations(List<Employee> employees) {
        var managerSalaryDifferenceEvaluators = employeeEvaluator.collectManagersEarningOutOfRecommendations(employees);
        reportManagersUnderpaid(managerSalaryDifferenceEvaluators);
        reportManagersOverpaid(managerSalaryDifferenceEvaluators);
    }

    private static void reportManagersUnderpaid(List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators) {
        System.out.println(REPORT_PATTERN__SUMMARY_MANAGERS_UNDERPAID.formatted(
                ManagerSalaryDifferenceEvaluator.getMinimumPlusSalaryRate()
        ));
        reportManagersOutOfSalaryRecommendations(managerSalaryDifferenceEvaluators, ManagerSalaryDifferenceEvaluator::isLessThanMinimum);
    }

    private static void reportManagersOverpaid(List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators) {
        System.out.println(REPORT_PATTERN__SUMMARY_MANAGERS_OVERPAID.formatted(
                ManagerSalaryDifferenceEvaluator.getMaximumPlusSalaryRate())
        );
        reportManagersOutOfSalaryRecommendations(managerSalaryDifferenceEvaluators, ManagerSalaryDifferenceEvaluator::isMoreThanMaximum);
    }

    private void reportEmployeesWithTooLongReportingLine(List<Employee> employees) {
        var employeeReportingLineEvaluators = employeeEvaluator.collectEmployeesWithTooLongReportingLines(employees);
        System.out.println(REPORT_PATTERN__SUMMARY_EMPLOYEES_WITH_TOO_LONG_REPORTING_LINE.formatted(
                EmployeeReportingLineEvaluator.MAXIMUM_RECOMMENDED_LENGTH
        ));

        employeeReportingLineEvaluators.forEach(evaluator -> {
            var employee = evaluator.getEmployee();
            System.out.println(REPORT_PATTERN__EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE.formatted(
                    employee.getFirstName(), employee.getLastName(),
                    employee.getId(),
                    evaluator.getReportingLine(),
                    evaluator.getDifference()
            ));
        });
    }

    private static void reportManagersOutOfSalaryRecommendations(
            List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators,
            Predicate<ManagerSalaryDifferenceEvaluator> filterPredicate
    ) {
        managerSalaryDifferenceEvaluators.stream()
                .filter(filterPredicate)
                .forEach(evaluator -> {
                    var manager = evaluator.getManager();

                    System.out.println(REPORT_PATTERN__MANAGER_PAID.formatted(
                            manager.getFirstName(), manager.getLastName(),
                            manager.getId(),
                            manager.getSalary(),
                            evaluator.getRelativePercentage(),
                            evaluator.getAverageSalaryOfSubordinates()
                    ));
                });
    }

}
