package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.model.EmployeeField;
import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.common.util.StringUtil;
import hu.epam.test.exercise.common.validation.AbstractListValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.epam.test.exercise.common.model.EmployeeField.*;
import static hu.epam.test.exercise.common.util.EmployeeUtil.*;

/**
 * Validator for making structural validation. This validator validates the raw data set for
 * <p>the number of filled fields,
 * <p>the mandatory datas,
 * <p>the numeric formats
 */
public class EmployeeStructuralValidator extends AbstractListValidator<String[]> {

    private static final String ERROR_MESSAGE_PATTERN__INDIVIDUAL_DETAILED = "Error(s) in validation of employee line %s\n";

    private static final String ERROR_MESSAGE_PATTERN__FIELD_IS_BLANK = "%s is mandatory, but missing.";

    private static final String ERROR_MESSAGE_PATTERN__NOT_A_NUMBER = "%s is expected to be a number, but is not.";

    private static final String ERROR_MESSAGE_PATTERN_CHUNK__FIELD_NUMBER_OF_LINE_DIFFERS_FROM_HEADER =
            "should have %s separated fields, but it has actually %s.";

    /**
     * Validates the datas for their structural validity described in the doc of {@link EmployeeStructuralValidator} class.
     *
     * @param employeeLines The list to validate.
     * @return A list of {@link ErrorMessage}s collecting all structural invalidities found.
     */
    @Override
    protected List<ErrorMessage> validateElements(List<String[]> employeeLines) {
        var errorMessages = new LinkedList<ErrorMessage>();
        errorMessages.addAll(validateEmployeeLinesGenerally(employeeLines));

        return errorMessages;
    }

    private List<ErrorMessage> validateEmployeeLinesGenerally(List<String[]> employeeLines) {
        var lineNumber = new AtomicInteger();

        return employeeLines.stream()
                .map(employeeLine -> validateEmployeeLine(employeeLine, lineNumber.incrementAndGet()))
                .filter(list -> !CollectionUtil.isEmpty(list))
                .map(errorMessages -> convertToJoinedIndividualErrorMessages(errorMessages, lineNumber.get()))
                .map(ErrorMessage::of)
                .toList();
    }

    private List<ErrorMessage> validateEmployeeLine(String[] employeeLine, int lineNumber) {
        if (areAllFieldsFilledOrManagerIdEmpty(employeeLine)) {
            return List.of(ErrorMessage.of(
                    PATTERN_LINE_NUMBER.formatted(lineNumber) +
                            ERROR_MESSAGE_PATTERN_CHUNK__FIELD_NUMBER_OF_LINE_DIFFERS_FROM_HEADER
                                    .formatted(getNumberOfFieldNames(), employeeLine.length)
            ));
        }

        return Stream.of(
                        validateNumber(employeeLine, ID),
                        validateText(employeeLine, FIRST_NAME),
                        validateText(employeeLine, LAST_NAME),
                        validateNumber(employeeLine, SALARY),
                        validateNullableNumber(employeeLine, MANAGER_ID)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

    }

    private boolean areAllFieldsFilledOrManagerIdEmpty(String[] employeeLine) {
        return employeeLine.length < getNumberOfFieldNames() - 1
                || employeeLine.length > getNumberOfFieldNames();
    }

    private Optional<ErrorMessage> validateNumber(String[] employeeLine, EmployeeField employeeField) {
        var textErrorMessageOpt = validateText(employeeLine, employeeField);

        if (textErrorMessageOpt.isPresent()) {
            return textErrorMessageOpt;
        }

        var numberText = getValue(employeeLine, employeeField);

        try {
            Integer.parseInt(numberText);
        } catch (NumberFormatException ex) {
            return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__NOT_A_NUMBER.formatted(employeeField.getFieldName())));
        }

        return Optional.empty();
    }

    private Optional<ErrorMessage> validateNullableNumber(String[] employeeLine, EmployeeField employeeField) {
        if (employeeLine.length <= indexOfFieldName(employeeField)) {
            return Optional.empty();
        }

        var numberText = getValue(employeeLine, employeeField);
        try {
            Integer.parseInt(numberText);
        } catch (NumberFormatException ex) {
            return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__NOT_A_NUMBER.formatted(employeeField.getFieldName())));
        }

        return Optional.empty();
    }

    private Optional<ErrorMessage> validateText(String[] employeeLine, EmployeeField employeeField) {
        var text = getValue(employeeLine, employeeField);
        if (StringUtil.isBlank(text)) {
            return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__FIELD_IS_BLANK.formatted(employeeField.getFieldName())));
        }

        return Optional.empty();
    }

    private String convertToJoinedIndividualErrorMessages(List<ErrorMessage> errorMessages, int lineNumber) {
        var detailedErrorMessages = errorMessages.stream()
                .map(errorMessage -> LINE_WITH_DOUBLE_INDENT.formatted(errorMessage.getText()))
                .collect(Collectors.joining(ERROR_MESSAGE_DELIMITER));

        return ERROR_MESSAGE_PATTERN__INDIVIDUAL_DETAILED.formatted(lineNumber) + detailedErrorMessages;
    }
}
