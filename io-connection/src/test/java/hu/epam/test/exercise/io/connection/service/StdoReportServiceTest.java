package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.operations.evaluation.EmployeeEvaluator;
import hu.epam.test.exercise.operations.evaluation.EmployeeReportingLineEvaluator;
import hu.epam.test.exercise.operations.evaluation.ManagerSalaryDifferenceEvaluator;
import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StdoReportServiceTest {

    private static final String DELIMITER_NEW_LINE = "\n";

    private final List<Employee> mockInputEmployeeList = Collections.emptyList();

    @Mock
    private EmployeeEvaluator mockEmployeeEvaluator;

    @Mock
    private PrintStream mockOutStream;

    private List<String> outLines;

    private StdoReportService stdoReportService;

    @BeforeEach
    void initEach() {
        System.setOut(mockOutStream);
        this.stdoReportService = new StdoReportService(mockEmployeeEvaluator);
        this.outLines = new LinkedList<>();

        reset(mockOutStream);

        doAnswer(method -> {
            outLines.add(method.getArgument(0));
            return null;
        }).when(mockOutStream)
                .println(any(String.class));
    }

    @Test
    void noEmployeeForAllSectionsTest() {
        mockEmployeeEvaluatorResutls(Collections.emptyList(), Collections.emptyList());

        stdoReportService.report(mockInputEmployeeList);

        assertEquals(
                """
                        Managers with less salary than 120.0% of the average salary of their subordinates:
                        \tNo employee found for this condition.
                        Managers with more salary than 150.0% of the average salary of their subordinates:
                        \tNo employee found for this condition.
                        Employees with reporting lines longer than 4:
                        \tNo employee found for this condition.""",
                joinOutLines(outLines)
        );
    }

    @Test
    void managersWithSalariesOutOfRecommendationsTest() {
        mockEmployeeEvaluatorResutls(
                List.of(
                        makeMockManagerSalaryDifferenceEvaluator(Employee.of(100, "John", "Doe", 71000, 47), new BigDecimal("60000")),
                        makeMockManagerSalaryDifferenceEvaluator(Employee.of(202, "Marty", "McFly", 35000, 47), new BigDecimal("20000")),
                        makeMockManagerSalaryDifferenceEvaluator(Employee.of(250, "Emmet", "Brown", 30000, 47), new BigDecimal("3444.35"))
                ),
                Collections.emptyList()
        );

        stdoReportService.report(mockInputEmployeeList);

        assertEquals(
                """
                        Managers with less salary than 120.0% of the average salary of their subordinates:
                        \tJohn Doe (100) earns 71000. The average salary of their subordinates is 60000. So the difference is -1000.0 from the recommendation.
                        Managers with more salary than 150.0% of the average salary of their subordinates:
                        \tMarty McFly (202) earns 35000. The average salary of their subordinates is 20000. So the difference is 5000.0 from the recommendation.
                        \tEmmet Brown (250) earns 30000. The average salary of their subordinates is 3444.35. So the difference is 24833.475 from the recommendation.
                        Employees with reporting lines longer than 4:
                        \tNo employee found for this condition.""",
                joinOutLines(outLines)
        );
    }

    @Test
    void employeesWithTooLongReportingLinesTest() {
        mockEmployeeEvaluatorResutls(Collections.emptyList(), List.of(
                makeMockEmployeeReportingLineEvaluator(Employee.of(100, "John", "Doe", 60000, 47), 5),
                makeMockEmployeeReportingLineEvaluator(Employee.of(202, "Marty", "McFly", 20000, 47), 12)
        ));

        stdoReportService.report(mockInputEmployeeList);

        assertEquals(
                """
                        Managers with less salary than 120.0% of the average salary of their subordinates:
                        \tNo employee found for this condition.
                        Managers with more salary than 150.0% of the average salary of their subordinates:
                        \tNo employee found for this condition.
                        Employees with reporting lines longer than 4:
                        \tJohn Doe (100) - 5 long reporting line (1 more)
                        \tMarty McFly (202) - 12 long reporting line (8 more)""",
                joinOutLines(outLines)
        );
    }

    private ManagerSalaryDifferenceEvaluator makeMockManagerSalaryDifferenceEvaluator(Employee manager, BigDecimal averageSalary) {
        var mock = mock(ManagerSalaryDifferenceEvaluator.class);

        lenient()
                .when(mock.getManager())
                .thenReturn(manager);
        lenient()
                .when(mock.getAverageSalaryOfSubordinates())
                .thenReturn(averageSalary);

        lenient()
                .when(mock.calculateDifferenceFromRecommendation())
                        .thenCallRealMethod();
        lenient()
                .when(mock.getManagerSalary())
                .thenReturn(BigDecimal.valueOf(manager.getSalary()));

        doCallRealMethod()
                .when(mock)
                .isLessThanMinimum();
        doCallRealMethod()
                .when(mock)
                .isMoreThanMaximum();

        return mock;
    }

    private EmployeeReportingLineEvaluator makeMockEmployeeReportingLineEvaluator(Employee employee, int reportingLine) {
        var mock = mock(EmployeeReportingLineEvaluator.class);

        doReturn(employee)
                .when(mock)
                .getEmployee();

        doReturn(reportingLine)
                .when(mock)
                .getReportingLine();

        doCallRealMethod()
                .when(mock)
                .getDifference();

        doCallRealMethod()
                .when(mock)
                .isLongerThanRecommended();

        return mock;
    }

    private void mockEmployeeEvaluatorResutls(
            List<ManagerSalaryDifferenceEvaluator> managerSalaryDifferenceEvaluators,
            List<EmployeeReportingLineEvaluator> employeeReportingLineEvaluators
    ) {
        doReturn(managerSalaryDifferenceEvaluators)
                .when(mockEmployeeEvaluator)
                .collectManagersEarningOutOfRecommendations(any());

        doReturn(employeeReportingLineEvaluators)
                .when(mockEmployeeEvaluator)
                .collectEmployeesWithTooLongReportingLines(any());
    }

    private String joinOutLines(List<String> outLines) {
        return String.join(DELIMITER_NEW_LINE, outLines);
    }
}
