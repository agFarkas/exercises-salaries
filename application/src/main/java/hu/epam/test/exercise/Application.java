package hu.epam.test.exercise;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.evaluation.operations.mapping.EmployeeMapper;
import hu.epam.test.exercise.evaluation.operations.validation.LogicalValidator;
import hu.epam.test.exercise.io.connection.operations.validation.StructuralValidator;
import hu.epam.test.exercise.service.FileReaderService;
import hu.epam.test.exercise.service.ReportService;
import org.springframework.stereotype.Service;

import static hu.epam.test.exercise.common.util.EmployeeUtil.getEmployeeLines;


@Service
public class Application {

    private final FileReaderService fileReaderService;

    private final StructuralValidator structuralValidator;
    private final LogicalValidator logicalValidator;

    private final EmployeeMapper employeeMapper;

    private final ReportService reportService;

    public Application(
            FileReaderService fileReaderService,
            StructuralValidator structuralValidator,
            LogicalValidator logicalValidator,
            EmployeeMapper employeeMapper,
            ReportService reportService
    ) {
        this.fileReaderService = fileReaderService;
        this.structuralValidator = structuralValidator;
        this.logicalValidator = logicalValidator;
        this.employeeMapper = employeeMapper;
        this.reportService = reportService;
    }

    public void run(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("No file path provided.");
                return;
            }

            var lines = fileReaderService.readTableLines(args[0]);
            structuralValidator.validate(lines);
            var employees = employeeMapper.mapEmployees(getEmployeeLines(lines));
            logicalValidator.validate(employees);

            reportService.report(employees);

        } catch (ValidationException ex) {
            System.out.println("Validation issues: ");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error while reporting: ");
            ex.printStackTrace();
        }
    }
}
