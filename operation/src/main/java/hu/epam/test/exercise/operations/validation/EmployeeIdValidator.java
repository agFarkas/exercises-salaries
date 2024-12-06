package hu.epam.test.exercise.operations.validation;

import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.StringUtil;
import hu.epam.test.exercise.common.validation.AbstractListValidator;
import hu.epam.test.exercise.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

import static hu.epam.test.exercise.common.DelimiterConstants.DELIMITER_COMMA_WITH_SPACE;

/**
 * Validator that makes a logical validation. Responsible for validating the uniqueness of employee ID-s.
 */
public class EmployeeIdValidator extends AbstractListValidator<Employee> {

    private static final String ERROR_MESSAGE_PATTERN__NON_UNIQUE_IDS = "The following Id-s are not unique: %s";

    /**
     * Validates the list of {@link Employee}s passed. If any employee ID is found by more than one employee, returns
     * one {@link ErrorMessage} in a list.
     *
     * @param employees The list to validate.
     * @return One {@link ErrorMessage} in a list that contains the ID-s of the CEO-s.
     */
    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();
        errorMessages.addAll(validateUniqueIds(employees));

        return errorMessages;
    }

    private List<ErrorMessage> validateUniqueIds(List<Employee> employees) {
        var nonUniqueIds = collectNonUniqueIds(employees);

        if (!StringUtil.isBlank(nonUniqueIds)) {
            return Collections.singletonList(
                    ErrorMessage.of(ERROR_MESSAGE_PATTERN__NON_UNIQUE_IDS.formatted(nonUniqueIds))
            );
        }

        return Collections.emptyList();
    }

    private String collectNonUniqueIds(List<Employee> employees) {
        return countIds(employees)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey().toString())
                .collect(Collectors.joining(DELIMITER_COMMA_WITH_SPACE));
    }

    private Map<Integer, Integer> countIds(List<Employee> employees) {
        var countsByIds = new HashMap<Integer, Integer>();

        employees.stream()
                .map(Employee::getId)
                .forEach(id -> countsByIds.put(id, countsByIds.getOrDefault(id, 0) + 1));

        return countsByIds;
    }

}
