package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.operations.evaluation.EmployeeEvaluator;
import hu.epam.test.exercise.operations.evaluation.EmployeeReportingLineEvaluator;
import hu.epam.test.exercise.operations.evaluation.ManagerSalaryDifferenceEvaluator;
import hu.epam.test.exercise.model.Employee;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Predicate;

/**
 * Report service for the evaluation. On this level, the output is not specified, it should be specifically determined
 * in the subclasses.
 */
public abstract class AbstractReportService {

    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_UNDERPAID = "Managers with less salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_MANAGERS_OVERPAID = "Managers with more salary than %s%% of the average salary of their subordinates:";
    private static final String REPORT_PATTERN__SUMMARY_EMPLOYEES_WITH_TOO_LONG_REPORTING_LINE = "Employees with reporting lines longer than %s:";

    private static final String REPORT_PATTERN__MANAGER_PAID = "\t%s %s (%s) earns %s. The average salary of their subordinates is %s. So the difference is %s from the recommendation.";
    private static final String REPORT_PATTERN__EMPLOYEE_WITH_TOO_LONG_REPORTING_LINE = "\t%s %s (%s) - %s long reporting line (%s more)";
    private static final String REPORT__NO_EMPLOYEE_FOUND = "\tNo employee found for this condition.";

    private final EmployeeEvaluator employeeEvaluator;
    private final PrintStream printStream;

    /**
     * @param employeeEvaluator is an implementor of the {@link EmployeeEvaluator}.
     * @param printStream       the output stream to print the report lines on.
     */
    protected AbstractReportService(EmployeeEvaluator employeeEvaluator, PrintStream printStream) {
        this.employeeEvaluator = employeeEvaluator;
        this.printStream = printStream;
    }

    /**
     * Executse the reporting by printing the results to the {@link PrintStream} specified by the subClass.
     *
     * @param employees The list of all {@link Employee}s to make the report about.
     */
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

        if (CollectionUtil.isEmpty(employeeReportingLineEvaluators)) {
            printStream.println(REPORT__NO_EMPLOYEE_FOUND);
        } else {
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
    }

    private void reportManagersOutOfSalaryRecommendations(
            List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators,
            Predicate<ManagerSalaryDifferenceEvaluator> filterPredicate
    ) {
        var managersWithSalaryOutOfRange = managerSalaryDifferenceEvaluators.stream()
                .filter(filterPredicate)
                .toList();

        if (CollectionUtil.isEmpty(managersWithSalaryOutOfRange)) {
            printStream.println(REPORT__NO_EMPLOYEE_FOUND);
        }

        managersWithSalaryOutOfRange.forEach(evaluator -> {
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
