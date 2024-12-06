package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import hu.epam.test.exercise.common.validation.AbstractListValidator;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static hu.epam.test.exercise.common.util.EmployeeUtil.FIELD_NAMES_JOINED;

/**
 * Validator for making structural validation. This validator validates the header line of the raw data set.
 */
public class HeaderValidator extends AbstractListValidator<String> {

    private static final String ERROR_MESSAGE_PATTERN__HEADLINE = "The file headline must be exactly as follows: %s";

    /**
     * Validates the header line of the data set.
     *
     * @param headers The list of field names in the header to validate.
     * @return One {@link ErrorMessage} in a list if it anyhow differs from the expected form.
     */
    @Override
    protected List<ErrorMessage> validateElements(List<String> headers) {

        if (!headersAreValid(headers)) {
            return Collections.singletonList(
                    ErrorMessage.of(ERROR_MESSAGE_PATTERN__HEADLINE.formatted(FIELD_NAMES_JOINED))
            );
        }

        return Collections.emptyList();
    }

    private boolean headersAreValid(List<String> headers) {
        return Objects.equals(headers, EmployeeUtil.getFieldNames());
    }
}
