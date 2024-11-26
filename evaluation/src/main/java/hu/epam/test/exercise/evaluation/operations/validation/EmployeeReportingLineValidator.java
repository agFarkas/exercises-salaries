package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.util.CollectionUtils;
import hu.epam.test.exercise.model.Employee;
import hu.epam.test.exercise.common.model.ErrorMessage;



import java.util.List;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EmployeeReportingLineValidator extends EmployeeLogicalValidator {

    private static final String CHAIN_DELIMITER = " -> ";
    private static final String ERROR_MESSAGE_PATTERN__CIRCULAR_REPORTING_LINE = "Circular reporting line by ids: %s";
    private static final String ERROR_MESSAGE_PATTERN__ASSIGNED_TO_THEMSELF = "Employee %s has assingned to themself as manager.";
    private static final String ERROR_MESSAGE_PATTERN__MANAGER_NOT_FOUND = "Manager %s not found by Id.";

    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();

        errorMessages.addAll(validateNoEmployeeAssignedToThemself(employees));

        if(CollectionUtils.isEmpty(errorMessages)){
            errorMessages.addAll(validateStraightReportingLines(employees));
        };

        return errorMessages;
    }

    private List<ErrorMessage> validateNoEmployeeAssignedToThemself(List<Employee> employees) {
        return employees.stream()
                .filter(emp -> Objects.equals(emp.getId(), emp.getManagerId()))
                .map(emp -> ErrorMessage.of(ERROR_MESSAGE_PATTERN__ASSIGNED_TO_THEMSELF.formatted(emp.getId())))
                .toList();
    }

    private List<ErrorMessage> validateStraightReportingLines(List<Employee> employees) {
        List<ErrorMessage> errorMessages = new LinkedList<>();

        for (var employee : employees) {
            if (!employee.isCEO()) {
                validateReportingLineOfEmployee(employees, employee)
                        .ifPresent(errorMessages::add);
            }
        }

        return errorMessages;
    }

    private Optional<ErrorMessage> validateReportingLineOfEmployee(List<Employee> employees, Employee initialEmployee) {
        var reportingLine = new LinkedHashSet<Employee>();
        var currentEmployee = new AtomicReference<>(initialEmployee);

        do {
            if (!reportingLine.add(currentEmployee.get())) {
                var employeeIdsJoined = concatStream(reportingLine, currentEmployee.get())
                        .map(employee -> Integer.toString(employee.getId()))
                        .collect(Collectors.joining(CHAIN_DELIMITER));
                return Optional.of(
                        ErrorMessage.of(ERROR_MESSAGE_PATTERN__CIRCULAR_REPORTING_LINE.formatted(employeeIdsJoined))
                );
            }

            var managerOpt = employees.stream()
                    .filter(emp -> Objects.equals(emp.getId(), currentEmployee.get().getManagerId()))
                    .findAny();
            if(managerOpt.isEmpty()) {
                return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__MANAGER_NOT_FOUND.formatted(currentEmployee.get().getManagerId())));
            }
            currentEmployee.set(managerOpt.get());

        } while (!currentEmployee.get().isCEO());

        return Optional.empty();
    }

    private static Stream<Employee> concatStream(LinkedHashSet<Employee> reportingLine, Employee currentEmployee) {
        return Stream.concat(reportingLine.stream(), Stream.of(currentEmployee));
    }
}
