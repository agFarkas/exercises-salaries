package hu.epam.test.exercise.operations.validation;

import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.model.Employee;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.epam.test.exercise.common.DelimiterConstants.DELIMITER_ARROW;

/**
 * Validator that makes a logical validation. Responsible for validating the finiteness of each reporting line.
 */
public class EmployeeReportingLineValidator extends AbstractListValidator<Employee> {

    private static final String ERROR_MESSAGE_PATTERN__CIRCULAR_REPORTING_LINE = "Circular reporting line by ids: %s";
    private static final String ERROR_MESSAGE_PATTERN__ASSIGNED_TO_THEMSELF = "Employee %s has assingned to themself as manager.";
    private static final String ERROR_MESSAGE_PATTERN__MANAGER_NOT_FOUND = "Manager %s not found by Id.";

    /**
     * Validates the list of {@link Employee}s passed. If any reporting line is fouind 'circular' (infinite), returns
     * a list of {@link ErrorMessage}s.
     *
     * @param employees The list to validate.
     * @return A list of {@link ErrorMessage}s collecting all the employees that are parts of a circular reporting line.
     */
    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();

        errorMessages.addAll(validateNoEmployeeAssignedToThemself(employees));

        if(CollectionUtil.isEmpty(errorMessages)){
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
                        .collect(Collectors.joining(DELIMITER_ARROW));
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
