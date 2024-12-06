package hu.epam.test.exercise.io.connection.operations.validation;


import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.StringUtil;
import hu.epam.test.exercise.common.validation.AbstractValidator;


public class ArgumentValidator extends AbstractValidator<String[]> {

    private static final String ERROR_MESSAGE__NO_FILE_PATH_PROVIDED = "No file path provided.";

    @Override
    public void validate(String[] args) {
        validateFileName(args);
    }

    private void validateFileName(String[] args) {
        if (args.length == 0 || StringUtil.isBlank(args[0])) {
            throw new ValidationException(ERROR_MESSAGE__NO_FILE_PATH_PROVIDED);
        }


    }

}
