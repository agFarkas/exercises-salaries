package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Evaluator class for operations about how long the employee's reporting line is. Its recommended maximum value is
 * defined in {@code MAXIMUM_RECOMMENDED_LENGTH} as a constant. The levels of the reporting line are counted up to the CEO.
 */
public class EmployeeReportingLineEvaluator {

    public static final int MAXIMUM_RECOMMENDED_LENGTH = 4;

    private final Employee employee;

    private final LinkedHashSet<Employee> reportingLine;

    public EmployeeReportingLineEvaluator(Employee employee, List<Employee> allEmployees) {
        this.employee = employee;
        this.reportingLine = makeReportingLine(employee, allEmployees);
    }

    /**
     * Checks if the employee's reporting line is longer than the recommended maximum length.
     *
     * @return {@code true} if the employee's reporting line is longer than the recommended maximum length.
     */
    public boolean isLongerThanRecommended() {
        return getReportingLine() > MAXIMUM_RECOMMENDED_LENGTH;
    }

    /**
     * Calculates the exact difference from the recommended maximum length of the employee's reporting line.
     *
     * @return The exact difference from the recommended maximum length of the employee's reporting line. If it is equal to
     * or under the recommended maximum, returns 0.
     */
    public int getDifference() {
        var actualDifference = getReportingLine() - MAXIMUM_RECOMMENDED_LENGTH;

        if (isLongerThanRecommended()) {
            return actualDifference;
        }

        return 0;
    }

    /**
     * @return The exact length of the employee's reporting line.
     */
    public int getReportingLine() {
        return reportingLine.size();
    }

    /**
     * @return The reference of the employee.
     */
    public Employee getEmployee() {
        return employee;
    }

    private LinkedHashSet<Employee> makeReportingLine(Employee employee, List<Employee> allEmployees) {
        var reportingLine = new LinkedHashSet<Employee>();
        var currentEmployee = new AtomicReference<>(employee);

        do {
            allEmployees.stream()
                    .filter(emp -> Objects.equals(emp.getId(), currentEmployee.get().getManagerId()))
                    .findAny()
                    .ifPresent(currentEmployee::set);

            reportingLine.add(currentEmployee.get());
        } while (!currentEmployee.get().isCEO());

        return reportingLine;
    }

}
