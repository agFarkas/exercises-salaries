package hu.epam.test.exercise;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.validation.AbstractValidator;
import hu.epam.test.exercise.evaluation.operations.mapping.AbstractListMapper;
import hu.epam.test.exercise.io.connection.service.AbstractInputReaderService;
import hu.epam.test.exercise.io.connection.service.StdoReportService;
import hu.epam.test.exercise.model.Employee;

import java.io.UncheckedIOException;
import java.util.List;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;


public class Application {

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
