package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EmployeeReportingLineEvaluator {

    public static final int MAXIMUM_RECOMMENDED_LENGTH = 4;

    private final Employee employee;

    private final LinkedHashSet<Employee> reportingLine;

    public EmployeeReportingLineEvaluator(Employee employee, List<Employee> allEmployees) {
        this.employee = employee;
        this.reportingLine = makeReportingLine(employee, allEmployees);
    }

    public boolean isLongerThanRecommended() {
        return getReportingLine() > MAXIMUM_RECOMMENDED_LENGTH;
    }


    public int getDifference() {
        var actualDifference = getReportingLine() - MAXIMUM_RECOMMENDED_LENGTH;

        if (isLongerThanRecommended()) {
            return actualDifference;
        }

        return 0;
    }

    public int getReportingLine() {
        return reportingLine.size();
    }

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
