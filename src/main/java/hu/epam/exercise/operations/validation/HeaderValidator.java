package hu.epam.exercise.operations.validation;

import hu.epam.exercise.model.ErrorMessage;
import hu.epam.exercise.util.EmployeeUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static hu.epam.exercise.util.EmployeeUtil.FIELD_NAMES_JOINED;

@Component
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
