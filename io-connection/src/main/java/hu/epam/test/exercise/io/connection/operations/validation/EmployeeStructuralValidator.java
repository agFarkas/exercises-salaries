package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.model.EmployeeField;
import hu.epam.test.exercise.common.model.ErrorMessage;
import hu.epam.test.exercise.common.util.CollectionUtils;
import hu.epam.test.exercise.common.util.StringUtils;
import hu.epam.test.exercise.common.validation.AbstractValidator;



import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.epam.test.exercise.common.model.EmployeeField.*;
import static hu.epam.test.exercise.common.util.EmployeeUtil.*;



public class EmployeeStructuralValidator extends AbstractValidator<String[]> {

    private static final String ERROR_MESSAGE_PATTERN__INDIVIDUAL_DETAILED = "Error(s) in validation of employee line %s\n";

    private static final String ERROR_MESSAGE_PATTERN__FIELD_IS_BLANK = "%s is blank, but must be provided.";

    private static final String ERROR_MESSAGE_PATTERN__NOT_A_NUMBER = "%s is expected to be a number, but is not.";

    private static final String ERROR_MESSAGE_PATTERN_CHUNK__FIELD_NUMBER_OF_LINE_DIFFERS_FROM_HEADER =
            "should have %s separated fields, but it has actually %s.";

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
                .filter(list -> !CollectionUtils.isEmpty(list))
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

    private Optional<ErrorMessage> validateNumber(String[] employeeLine, EmployeeField fieldName) {
        var textErrorMessageOpt = validateText(employeeLine, fieldName);

        if (textErrorMessageOpt.isPresent()) {
            return textErrorMessageOpt;
        }

        var numberText = getValue(employeeLine, fieldName);

        try {
            Integer.parseInt(numberText);
        } catch (NumberFormatException ex) {
            return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__NOT_A_NUMBER.formatted(fieldName.getFieldName())));
        }

        return Optional.empty();
    }

    private Optional<ErrorMessage> validateNullableNumber(String[] employeeLine, EmployeeField fieldName) {
        if(employeeLine.length <= indexOfFieldName(fieldName)) {
            return Optional.empty();
        }

        var numberText = getValue(employeeLine, fieldName);
            try {
                Integer.parseInt(numberText);
            } catch (NumberFormatException ex) {
                return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__NOT_A_NUMBER.formatted(fieldName.getFieldName())));
            }

        return Optional.empty();
    }

    private Optional<ErrorMessage> validateText(String[] employeeLine, EmployeeField fieldName) {
        var text = getValue(employeeLine, fieldName);
        if (StringUtils.isBlank(text)) {
            return Optional.of(ErrorMessage.of(ERROR_MESSAGE_PATTERN__FIELD_IS_BLANK.formatted(fieldName.getFieldName())));
        }

        return Optional.empty();
    }

    private String convertToJoinedIndividualErrorMessages(List<ErrorMessage> errorMessages, int lineNumber) {
        var detailedErrorMessages = errorMessages.stream()
                .map(errorMessage -> "\t\t" + errorMessage.getText())
                .collect(Collectors.joining(ERROR_MESSAGE_DELIMITER));

        return ERROR_MESSAGE_PATTERN__INDIVIDUAL_DETAILED.formatted(lineNumber) + detailedErrorMessages;
    }
}
