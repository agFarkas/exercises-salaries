package hu.epam.test.exercise.operations.validation;

import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.model.Employee;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Validator that makes a logical validation. Responsible for validating the entire data set that it contains exactly one CEO.
 * (neither less nor more)
 */
public class EmployeeCEOValidator extends AbstractListValidator<Employee> {

    private static final String ERROR_MESSAGE_PATTERN__CEOS_GENERAL = "There must be exactly one CEO. ";
    private static final String ERROR_MESSAGE_PATTERN__CEOS_NO =
            ERROR_MESSAGE_PATTERN__CEOS_GENERAL + "No CEO was actually provided.";

    private static final String ERROR_MESSAGE_PATTERN__CEOS_MULTIPLE =
            ERROR_MESSAGE_PATTERN__CEOS_GENERAL + "Multiple CEOs were actually provided as follows by ID: %s";


    /**
     * Validates the list of {@link Employee}s passed. If it finds no CEO or more CEO-s than one, returns one {@link ErrorMessage}
     * in a list.
     *
     * @param employees The list to validate.
     * @return One {@link ErrorMessage} in a list that contains the ID-s of the CEO-s.
     */
    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        return new LinkedList<>(validateCEO(employees));
    }

    private List<ErrorMessage> validateCEO(List<Employee> employees) {
        var ceoIds = employees.stream()
                .filter(employee -> Objects.isNull(employee.getManagerId()))
                .map(Employee::getId)
                .toList();

        if (CollectionUtil.isEmpty(ceoIds)) {
            return Collections.singletonList(
                    ErrorMessage.of(ERROR_MESSAGE_PATTERN__CEOS_NO)
            );
        } else if (ceoIds.size() > 1) {
            return Collections.singletonList(
                    ErrorMessage.of(ERROR_MESSAGE_PATTERN__CEOS_MULTIPLE.formatted(ceoIds))
            );
        }

        return Collections.emptyList();
    }
}
