package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeEvaluator;

public class StdoReportService extends AbstractReportService {

    public StdoReportService(EmployeeEvaluator employeeEvaluator) {
        super(employeeEvaluator, System.out);
    }
}
