package hu.epam.test.exercise;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.validation.AbstractValidator;
import hu.epam.test.exercise.operations.evaluation.EmployeeEvaluatorImpl;
import hu.epam.test.exercise.operations.mapping.AbstractListMapper;
import hu.epam.test.exercise.operations.mapping.EmployeeListMapper;
import hu.epam.test.exercise.operations.validation.EmployeeCEOValidator;
import hu.epam.test.exercise.operations.validation.EmployeeIdValidator;
import hu.epam.test.exercise.operations.validation.EmployeeReportingLineValidator;
import hu.epam.test.exercise.operations.validation.LogicalValidator;
import hu.epam.test.exercise.io.connection.operations.validation.ArgumentValidator;
import hu.epam.test.exercise.io.connection.operations.validation.EmployeeStructuralValidator;
import hu.epam.test.exercise.io.connection.operations.validation.HeaderValidator;
import hu.epam.test.exercise.io.connection.operations.validation.StructuralValidator;
import hu.epam.test.exercise.io.connection.service.AbstractInputReaderService;
import hu.epam.test.exercise.io.connection.service.FileReaderService;
import hu.epam.test.exercise.io.connection.service.StdoReportService;
import hu.epam.test.exercise.model.Employee;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;

/**
 * Defines the architecture of the business logic and the logical order of the operations to execute.
 */
public class Application {

    private static Application instance;

    private final AbstractValidator<String[]> argumentValidator;
    private final AbstractInputReaderService inputReaderService;

    private final AbstractValidator<List<String[]>> structuralValidator;
    private final AbstractValidator<List<Employee>> logicalValidator;

    private final AbstractListMapper<String[], Employee> employeeListMapper;

    private final StdoReportService reportService;

    public Application(
            AbstractValidator<String[]> argumentValidator,
            AbstractInputReaderService inputReaderService,
            AbstractValidator<List<String[]>> structuralValidator,
            AbstractValidator<List<Employee>> logicalValidator,
            AbstractListMapper<String[], Employee> employeeListMapper,
            StdoReportService reportService
    ) {
        this.argumentValidator = argumentValidator;
        this.inputReaderService = inputReaderService;
        this.structuralValidator = structuralValidator;
        this.logicalValidator = logicalValidator;
        this.employeeListMapper = employeeListMapper;
        this.reportService = reportService;
    }

    /**
     * @return A certain composition of the Application as a singleton instance.
     */
    public static Application of() {
        if (Objects.isNull(instance)) {
            instance = createApplicationInstance();
        }

        return instance;
    }

    private static Application createApplicationInstance() {
        return new Application(
                new ArgumentValidator(),
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
                new EmployeeListMapper(),
                new StdoReportService(
                        new EmployeeEvaluatorImpl()
                )
        );
    }

    /**
     * executes the process for the given inputs in the order of validation, mapping evaluation, reporting.
     * The validation has three phases:
     * <p>1) argument validation: the very first step is validting the given input arguments before any kind of process launches.
     * <p>2) structural validation: right after obtaining the raw data from the input. This is the validation of the raw
     * string datas on existence of mandatory values and, where required, the format (e.g. number)
     * <p>3) logical validation: executed after mapping the data into objects in the corresponding formats (e.g. string and numbers).
     * This type of validation validates the logical expectations for the data set (e.g. exactly 1 CEO expected, etc.).
     *
     * @param args are the arguments provided by the user by launching the application
     */
    public void run(String[] args) {
        try {
            argumentValidator.validate(args);

            var lines = inputReaderService.readTableLines(args[0]);
            structuralValidator.validate(lines);

            var employees = employeeListMapper.mapAll(getEmployeeLines(lines));
            logicalValidator.validate(employees);

            reportService.report(employees);
        } catch (UncheckedIOException ex) {
            System.out.println("Error with resources:");
            ex.printStackTrace();
        } catch (ValidationException ex) {
            System.out.println("Validation issues:");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error while reporting:");
            ex.printStackTrace();
        }
    }
}
