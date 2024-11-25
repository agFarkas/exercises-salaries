package hu.epam.exercise.operations.evaluation;

import hu.epam.exercise.model.Employee;

import java.util.LinkedHashSet;

public class EmployeeReportingLineEvaluator {

    public static final int MAXIMUM_RECOMMENDED_LENGTH = 4;

    private final Employee employee;

    private final LinkedHashSet<Employee> reportingLine;


    public EmployeeReportingLineEvaluator(Employee employee, LinkedHashSet<Employee> reportingLine) {
        this.employee = employee;
        this.reportingLine = reportingLine;
    }

    public boolean isLongerThanRecommended() {
        return getReportingLine() > MAXIMUM_RECOMMENDED_LENGTH;
    }


    public int getDifference() {
        return getReportingLine() - MAXIMUM_RECOMMENDED_LENGTH;
    }

    public int getReportingLine() {
        return reportingLine.size();
    }

    public Employee getEmployee() {
        return employee;
    }

}
