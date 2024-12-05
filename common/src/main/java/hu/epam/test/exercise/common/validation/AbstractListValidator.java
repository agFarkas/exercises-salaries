package hu.epam.test.exercise.common.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractListValidator<T> extends AbstractValidator<List<T>> {

    private static final String ERROR_MESSAGE_PATTERN__SUMMARY = "Error(s) in validation:\n%s";
    protected static final String PATTERN_LINE_NUMBER = "Line %s ";
    protected static final String LINE_WITH_SIMPLE_INDENT = "\t%s";
    protected static final String LINE_WITH_DOUBLE_INDENT = "\t\t%s";
    protected static final String ERROR_MESSAGE_DELIMITER = "\n";

    @Override
    public void validate(List<T> elements) {
        var errorMessages = validateElements(elements);

        requireNoError(errorMessages);
    }

    protected abstract List<ErrorMessage> validateElements(List<T> elements);

    private void requireNoError(List<ErrorMessage> errorMessages) {
        if (!CollectionUtil.isEmpty(errorMessages)) {
            throw new ValidationException(ERROR_MESSAGE_PATTERN__SUMMARY.formatted(
                    joinErrorMessageTexts(errorMessages)
            ));
        }
    }

    private String joinErrorMessageTexts(List<ErrorMessage> errorMessages) {
        return errorMessages.stream()
                .map(errorMessage -> LINE_WITH_SIMPLE_INDENT.formatted(errorMessage.getText()))
                .collect(Collectors.joining(ERROR_MESSAGE_DELIMITER));
    }
}
