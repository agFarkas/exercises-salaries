package hu.epam.test.exercise.io.connection.operations.validation;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentValidatorTest extends TestParent {

    private final ArgumentValidator argumentValidator = new ArgumentValidator();

    @Test
    void validArgumentsTest() {
        assertDoesNotThrow(() -> argumentValidator.validate(new String[]{"any-file-name.txt"}));
    }

    @Test
    void rejectReadingFileWithNullFileNameTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> argumentValidator.validate(new String[]{})
        );
        assertEquals("No file path provided.", exception.getMessage());
    }

    @Test
    void rejectReadingFileWithBlankFileNameTest() {
        var exception = assertThrowsExactly(
                ValidationException.class,
                () -> argumentValidator.validate(new String[]{" "})
        );

        assertEquals("No file path provided.", exception.getMessage());
    }
}
