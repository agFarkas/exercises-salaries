package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.model.Employee;
import hu.epam.test.exercise.common.model.ErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class EmployeeCEOValidator extends EmployeeLogicalValidator {

    private static final String ERROR_MESSAGE_PATTERN__CEOS_GENERAL = "There must be exactly one CEO. ";
    private static final String ERROR_MESSAGE_PATTERN__CEOS_NO =
            ERROR_MESSAGE_PATTERN__CEOS_GENERAL + "No CEO was actually provided.";

    private static final String ERROR_MESSAGE_PATTERN__CEOS_MULTIPLE =
            ERROR_MESSAGE_PATTERN__CEOS_GENERAL + "Multiple CEOs were actually provided as follows by ID: %s";

    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();
        errorMessages.addAll(validateCEO(employees));

        return errorMessages;
    }

    private List<ErrorMessage> validateCEO(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();

        var ceoIds = employees.stream()
                .filter(employee -> Objects.isNull(employee.getManagerId()))
                .map(Employee::getId)
                .toList();

        if (CollectionUtils.isEmpty(ceoIds)) {
            errorMessages.add(ErrorMessage.of(ERROR_MESSAGE_PATTERN__CEOS_NO));
        } else if (ceoIds.size() > 1) {
            errorMessages.add(ErrorMessage.of(ERROR_MESSAGE_PATTERN__CEOS_MULTIPLE.formatted(ceoIds)));
        }

        return errorMessages;
    }
}
