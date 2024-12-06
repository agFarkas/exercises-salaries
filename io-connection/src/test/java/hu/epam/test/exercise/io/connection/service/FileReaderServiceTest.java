package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class FileReaderServiceTest extends TestParent {

    @Test
    void readFileTest() {
        var resourceFilePath = getAbsoluteFilePathOfResource("employees-valid.csv");
        var fileReaderService = new FileReaderService();

        var lines = fileReaderService.readTableLines(resourceFilePath);
        assertEquals(6, lines.size());
        assertEquals(5, lines.getFirst().length);
    }

    @Test
    void rejectReadingFileWithInvalidFileNameTest() {
        assertThrowsExactly(
                UncheckedIOException.class,
                () -> new FileReaderService()
                        .readTableLines("invalid-dummy.csv")
        );
    }
}
