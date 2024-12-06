package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.operations.evaluation.EmployeeEvaluator;

/**
 * Report service to report on the standard output (console)
 */
public class StdoReportService extends AbstractReportService {

    public StdoReportService(EmployeeEvaluator employeeEvaluator) {
        super(employeeEvaluator, System.out);
    }
}
