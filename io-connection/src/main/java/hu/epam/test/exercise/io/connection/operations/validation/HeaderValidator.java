package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import hu.epam.test.exercise.common.validation.AbstractValidator;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static hu.epam.test.exercise.common.util.EmployeeUtil.FIELD_NAMES_JOINED;



public class HeaderValidator extends AbstractValidator<String> {

    private static final String ERROR_MESSAGE_PATTERN__HEADLINE = "The file headline must be exactly as follows: %s";

    @Override
    protected List<ErrorMessage> validateElements(List<String> headers) {
        var errorMessages = new LinkedList<ErrorMessage>();

        if (!headersAreValid(headers)) {
            errorMessages.add(
                    ErrorMessage.of(ERROR_MESSAGE_PATTERN__HEADLINE.formatted(FIELD_NAMES_JOINED))
            );
        }

        return errorMessages;
    }

    private boolean headersAreValid(List<String> headers) {
        return Objects.equals(headers, EmployeeUtil.getFieldNames());
    }
}
