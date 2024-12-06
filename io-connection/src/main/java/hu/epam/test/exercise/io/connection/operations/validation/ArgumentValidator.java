package hu.epam.test.exercise.io.connection.operations.validation;


import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.StringUtil;
import hu.epam.test.exercise.common.validation.AbstractValidator;

/**
 * Validator for making validation over the arguments passed by the user.
 */
public class ArgumentValidator extends AbstractValidator<String[]> {

    private static final String ERROR_MESSAGE__NO_FILE_PATH_PROVIDED = "No file path provided.";

    /**
     * Validates the argument array passed to the application by the user. Only the absolute path of the input file is required.
     *
     * @param args the {@link String} array of the arguments passed to the application by the user.
     */
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
