package hu.epam.test.exercise;

import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeEvaluator;
import hu.epam.test.exercise.evaluation.operations.mapping.EmployeeMapper;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeCEOValidator;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeIdValidator;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeReportingLineValidator;
import hu.epam.test.exercise.evaluation.operations.validation.LogicalValidator;
import hu.epam.test.exercise.io.connection.operations.validation.EmployeeStructuralValidator;
import hu.epam.test.exercise.io.connection.operations.validation.HeaderValidator;
import hu.epam.test.exercise.io.connection.operations.validation.StructuralValidator;
import hu.epam.test.exercise.service.FileReaderService;
import hu.epam.test.exercise.service.ReportService;

public class Main {

    public static void main(String[] args) {
        new Application(
                new FileReaderService(),
                new StructuralValidator(
                        new HeaderValidator(),
                        new EmployeeStructuralValidator()
                ),
                new LogicalValidator(
                        new EmployeeIdValidator(),
                        new EmployeeCEOValidator(),
                        new EmployeeReportingLineValidator()
                ),
                new EmployeeMapper(),
                new ReportService(
                        new EmployeeEvaluator()
                )
        ).run(args);
    }

}