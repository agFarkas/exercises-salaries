package hu.epam.test.exercise.service;

import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileReaderServiceTest extends TestParent {

    private final FileReaderService fileReaderService = new FileReaderService();

    @Test
    void readFileTest() {
        var resourceFilePath = getAbsoluteFilePathOfResource("employees-valid.csv");

        var lines = fileReaderService.readTableLines(resourceFilePath);
        assertEquals(6, lines.size());
        assertEquals(5, lines.getFirst().length);
    }

    @Test
    void rejectReadingFileWithNullFileNameTest() {
        var exception = assertThrowsExactly(
                RuntimeException.class,
                () -> fileReaderService.readTableLines(null)
        );
        assertEquals("The filename is blank!", exception.getMessage());
    }

    @Test
    void rejectReadingFileWithBlankFileNameTest() {
        var exception = assertThrowsExactly(
                RuntimeException.class,
                () -> fileReaderService.readTableLines(" ")
        );

        assertEquals("The filename is blank!", exception.getMessage());
    }

    @Test
    void rejectReadingFileWithInvalidFileNameTest() {
        assertThrowsExactly(
                UncheckedIOException.class,
                () -> fileReaderService.readTableLines("invalid-dummy.csv")
        );
    }
}
