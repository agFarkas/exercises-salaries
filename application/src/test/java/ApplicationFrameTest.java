import hu.epam.test.exercise.Application;
import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.evaluation.operations.mapping.EmployeeListMapper;
import hu.epam.test.exercise.evaluation.operations.validation.LogicalValidator;
import hu.epam.test.exercise.io.connection.operations.validation.ArgumentValidator;
import hu.epam.test.exercise.io.connection.operations.validation.StructuralValidator;
import hu.epam.test.exercise.io.connection.service.FileReaderService;
import hu.epam.test.exercise.io.connection.service.StdoReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ApplicationFrameTest {

    private static final String DELIMITER_NEW_LINE = "\n";

    private Vector<String> outLines;

    @Mock
    private ArgumentValidator mockArgumentValidator;

    @Mock
    private FileReaderService mockFileReaderService;

    @Mock
    private StructuralValidator mockStructuralValidator;

    @Mock
    private LogicalValidator mockLogicalValidator;

    @Mock
    private EmployeeListMapper mockEmployeeListMapper;

    @Mock
    private StdoReportService mockStdoReportService;

    @Mock
    private PrintStream mockOutStream;

    @Mock
    private PrintStream mockErrStream;

    private Application application;

    @BeforeEach
    void initEach() {
        System.setOut(mockOutStream);
        System.setErr(mockErrStream);

        this.application = new Application(
                mockArgumentValidator,
                mockFileReaderService,
                mockStructuralValidator,
                mockLogicalValidator,
                mockEmployeeListMapper,
                mockStdoReportService
        );

        this.outLines = new Vector<>();

        doAnswer(method -> {
            outLines.add(method.getArgument(0));
            return null;
        }).when(mockOutStream)
                .println(any(String.class));

        doAnswer(method -> {
            Exception argument = method.getArgument(0);
            outLines.add(argument.toString());
            return null;
        }).when(mockErrStream)
                .println(any(Exception.class));
    }

    @Test
    void validationExceptionPrintTest() {
        doThrow(new ValidationException("Dummy validation error message."))
                .when(mockArgumentValidator).validate(any());

        application.run(new String[0]);

        assertEquals(List.of(
                "Validation issues:",
                "hu.epam.test.exercise.common.exception.ValidationException: Dummy validation error message."
        ), outLines);
    }

    @Test
    void ioExceptionPrintTest() {
        doThrow(new UncheckedIOException(
                new FileNotFoundException("Dummy file not found.")
        )).when(mockFileReaderService)
                .readTableLines(any());

        application.run(new String[]{"DummyFile.csv"});

        assertEquals(List.of(
                "Error with resources:",
                "java.io.UncheckedIOException: java.io.FileNotFoundException: Dummy file not found."
        ), outLines);
    }

    @Test
    void otherExceptionPrintTest() {
        when(mockFileReaderService.readTableLines(any()))
                .thenReturn(List.of(
                        new String[]{"Id", "firstName", "lastName", "salary", "managerId"},
                        new String[]{"1", "John", "Doe", "60000"}
                ));

        doThrow(new RuntimeException("Some other kind of dummy exception happens."))
                .when(mockEmployeeListMapper)
                .mapAll(any());

        application.run(new String[]{"DummyFile.csv"});

        assertEquals(List.of(
                "Error while reporting:",
                "java.lang.RuntimeException: Some other kind of dummy exception happens."
        ), outLines);
    }
}
