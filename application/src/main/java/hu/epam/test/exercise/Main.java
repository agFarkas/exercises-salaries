package hu.epam.test.exercise;

import hu.epam.test.exercise.evaluation.operations.evaluation.EmployeeEvaluatorImpl;
import hu.epam.test.exercise.evaluation.operations.mapping.EmployeeListMapper;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeCEOValidator;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeIdValidator;
import hu.epam.test.exercise.evaluation.operations.validation.EmployeeReportingLineValidator;
import hu.epam.test.exercise.evaluation.operations.validation.LogicalValidator;
import hu.epam.test.exercise.io.connection.operations.validation.EmployeeStructuralValidator;
import hu.epam.test.exercise.io.connection.operations.validation.HeaderValidator;
import hu.epam.test.exercise.io.connection.operations.validation.StructuralValidator;
import hu.epam.test.exercise.io.connection.service.FileReaderService;
import hu.epam.test.exercise.io.connection.service.StdoReportService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file path provided.");
            return;
        }

        new Application(
                new FileReaderService(args[0]),
                new StructuralValidator(
                        new HeaderValidator(),
                        new EmployeeStructuralValidator()
                ),
                new LogicalValidator(
                        new EmployeeIdValidator(),
                        new EmployeeCEOValidator(),
                        new EmployeeReportingLineValidator()
                ),
                new EmployeeListMapper(),
                new StdoReportService(
                        new EmployeeEvaluatorImpl()
                )
        ).run();
    }

}