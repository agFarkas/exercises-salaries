package hu.epam.test.exercise.common.exception;

/**
 * Thrown by validators in the applications if finding a part of the dataset invalid
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
