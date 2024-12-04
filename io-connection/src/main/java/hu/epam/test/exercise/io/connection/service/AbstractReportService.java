package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeEvaluator;
import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeReportingLineEvaluator;
import hu.epam.test.exercise.evaluation.operations.evaluation.ManagerSalaryDifferenceEvaluator;
import hu.epam.test.exercise.model.Employee;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractReportService {

    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_UNDERPAID = "Managers with less salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_OVERPAID = "Managers with more salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_EMPLOYEES_WITH_TOO_LONG_REPORTING_LINE = "Employees with reporting lines longer than %s:";

    private static final String REPORT_PATTERN__MANAGER_PAID = "\t%s %s (%s) earns %s. The average salary of their subordinates is %s. So the difference is %s from the recommendation.";
    private static final String REPORT_PATTERN__EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE = "\t%s %s (%s) - %s long reporting line (%s more)";

    private final EmployeeEvaluator employeeEvaluator;
    private final PrintStream printStream;

    protected AbstractReportService(EmployeeEvaluator employeeEvaluator, PrintStream printStream) {
        this.employeeEvaluator = employeeEvaluator;
        this.printStream = printStream;
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

    private void reportManagersUnderpaid(List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators) {
        printStream.println(REPORT_PATTERN__SUMMARY_MANAGERS_UNDERPAID.formatted(
                ManagerSalaryDifferenceEvaluator.getMinimumPlusSalaryRate()
        ));
        reportManagersOutOfSalaryRecommendations(managerSalaryDifferenceEvaluators, ManagerSalaryDifferenceEvaluator::isLessThanMinimum);
    }

    private void reportManagersOverpaid(List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators) {
        printStream.println(REPORT_PATTERN__SUMMARY_MANAGERS_OVERPAID.formatted(
                ManagerSalaryDifferenceEvaluator.getMaximumPlusSalaryRate())
        );
        reportManagersOutOfSalaryRecommendations(managerSalaryDifferenceEvaluators, ManagerSalaryDifferenceEvaluator::isMoreThanMaximum);
    }

    private void reportEmployeesWithTooLongReportingLine(List<Employee> employees) {
        var employeeReportingLineEvaluators = employeeEvaluator.collectEmployeesWithTooLongReportingLines(employees);
        printStream.println(REPORT_PATTERN__SUMMARY_EMPLOYEES_WITH_TOO_LONG_REPORTING_LINE.formatted(
                EmployeeReportingLineEvaluator.MAXIMUM_RECOMMENDED_LENGTH
        ));

        employeeReportingLineEvaluators.forEach(evaluator -> {
            var employee = evaluator.getEmployee();
            printStream.println(REPORT_PATTERN__EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE.formatted(
                    employee.getFirstName(), employee.getLastName(),
                    employee.getId(),
                    evaluator.getReportingLine(),
                    evaluator.getDifference()
            ));
        });
    }

    private void reportManagersOutOfSalaryRecommendations(
            List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators,
            Predicate<ManagerSalaryDifferenceEvaluator> filterPredicate
    ) {
        managerSalaryDifferenceEvaluators.stream()
                .filter(filterPredicate)
                .forEach(evaluator -> {
                    var manager = evaluator.getManager();

                    printStream.println(REPORT_PATTERN__MANAGER_PAID.formatted(
                            manager.getFirstName(), manager.getLastName(),
                            manager.getId(),
                            manager.getSalary(),
                            evaluator.getAverageSalaryOfSubordinates(),
                            evaluator.calculateDifferenceFromRecommendation()
                    ));
                });
    }
}
