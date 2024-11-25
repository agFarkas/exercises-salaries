package hu.epam.exercise.operations.validation;

import hu.epam.exercise.exception.ValidationException;
import hu.epam.exercise.model.ErrorMessage;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractValidator<T> {
    protected static final String PATTERN_LINE_NUMBER = "Line %s ";
    protected static final String ERROR_MESSAGE_DELIMITER = "\n";
    private static final String ERROR_MESSAGE_PATTERN__SUMMARY = "Error(s) in validation:\n%s";

    public void validate(List<T> elements) {
        var errorMessages = validateElements(elements);

        requireNoError(errorMessages);
    }

    protected abstract List<ErrorMessage> validateElements(List<T> elements);

    private void requireNoError(List<ErrorMessage> errorMessages) {
        if (!CollectionUtils.isEmpty(errorMessages)) {
            var errorMessagesJoined = errorMessages.stream()
                    .map(errorMessage -> "\t" + errorMessage.getText())
                    .collect(Collectors.joining(ERROR_MESSAGE_DELIMITER));

            throw new ValidationException(ERROR_MESSAGE_PATTERN__SUMMARY.formatted(errorMessagesJoined));
        }
    }
}
