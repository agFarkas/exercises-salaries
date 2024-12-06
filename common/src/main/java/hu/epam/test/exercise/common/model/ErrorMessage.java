package hu.epam.test.exercise.common.model;

/**
 * Specific object carrying error message
 */
public class ErrorMessage {

    private final String text;

    private ErrorMessage(String text) {
        this.text = text;
    }

    /**
     * Creates an instance of {@link ErrorMessage}.
     *
     * @param text The message text
     * @return new instance of {@link ErrorMessage}
     */
    public static ErrorMessage of(String text) {
        return new ErrorMessage(text);
    }

    public String getText() {
        return text;
    }

}
