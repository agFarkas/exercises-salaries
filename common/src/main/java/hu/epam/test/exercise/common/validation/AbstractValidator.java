package hu.epam.test.exercise.common.validation;

/**
 * Vaidator for lists of elements.
 * @param <T> Generically defines the type of the expected value to validate.
 */
public abstract class AbstractValidator<T> {

    /**
     * @implNote Should validate the value passed, and escalate a {@link hu.epam.test.exercise.common.exception.ValidationException},
     * if it finds one or more invalid elements. Suggested to use {@link hu.epam.test.exercise.common.model.ErrorMessage} objects
     * while collecting the invalidities.
     * @param value
     */
    public abstract void validate(T value);
}
