package hu.epam.test.exercise.common.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Vaidator for lists of elements.
 * @param <T> Generically defines the type of the expected elements.
 */
public abstract class AbstractListValidator<T> extends AbstractValidator<List<T>> {

    private static final String ERROR_MESSAGE_PATTERN__SUMMARY = "Error(s) in validation:\n%s";
    protected static final String PATTERN_LINE_NUMBER = "Line %s ";
    protected static final String LINE_WITH_SIMPLE_INDENT = "\t%s";
    protected static final String LINE_WITH_DOUBLE_INDENT = "\t\t%s";
    protected static final String ERROR_MESSAGE_DELIMITER = "\n";


    /**
     * Validates a list of elements with the specified type.
     * If getting any {@link ErrorMessage} of one or more invalid elements, escalates a {@link ValidationException}
     * @param elements The list to validate.
     */
    @Override
    public void validate(List<T> elements) {
        var errorMessages = validateElements(elements);

        requireNoError(errorMessages);
    }

    /**
     * A specific process for the validation of the list.
     *
     * @implSpec Should return an empty list if no element was found invalid. Should collect all {@link ErrorMessage}s
     * by detected invalidities, so that the user gets as many feedbacks for corrections at once as possible by the validator.
     *
     * @param elements The list to validate.
     * @return a list of {@link ErrorMessage}s.
     */
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
