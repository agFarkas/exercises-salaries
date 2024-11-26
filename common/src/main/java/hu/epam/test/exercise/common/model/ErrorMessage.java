package hu.epam.test.exercise.common.model;

public class ErrorMessage {

    private final String text;

    private ErrorMessage(String text) {
        this.text = text;
    }

    public static ErrorMessage of(String text) {
        return new ErrorMessage(text);
    }

    public String getText() {
        return text;
    }

}
