package hu.epam.test.exercise.evaluation.operations.validation;

import hu.epam.test.exercise.common.util.StringUtil;
import hu.epam.test.exercise.model.Employee;
import hu.epam.test.exercise.common.model.ErrorMessage;


import java.util.*;
import java.util.stream.Collectors;

public class EmployeeIdValidator extends EmployeeLogicalValidator {

    private static final String DELIMITER = ", ";
    private static final String ERROR_MESSAGE_PATTERN__NON_UNIQUE_IDS = "The following Id-s are not unique: %s";

    @Override
    protected List<ErrorMessage> validateElements(List<Employee> employees) {
        var errorMessages = new LinkedList<ErrorMessage>();
        errorMessages.addAll(validateUniqueIds(employees));

        return errorMessages;
    }

    private List<ErrorMessage> validateUniqueIds(List<Employee> employees) {
        var nonUniqueIds = collectNonUniqueIds(employees);

        if (!StringUtil.isBlank(nonUniqueIds)) {
            return Collections.singletonList(ErrorMessage.of(ERROR_MESSAGE_PATTERN__NON_UNIQUE_IDS.formatted(nonUniqueIds)));
        }

        return Collections.emptyList();
    }

    private String collectNonUniqueIds(List<Employee> employees) {
        return countIds(employees)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey().toString())
                .collect(Collectors.joining(DELIMITER));
    }

    private Map<Integer, Integer> countIds(List<Employee> employees) {
        var countsByIds = new HashMap<Integer, Integer>();

        employees.stream()
                .map(Employee::getId)
                .forEach(id -> countsByIds.put(id, countsByIds.getOrDefault(id, 0) + 1));

        return countsByIds;
    }

}
