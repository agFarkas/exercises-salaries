package hu.epam.test.exercise.service;

import hu.epam.test.exercise.io.connection.TestParent;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileReaderServiceTest extends TestParent {

    private final FileReaderService fileReaderService = new FileReaderService();

    @Test
    void readFileTest() {
        var resourceFilePath = getAbsoluteFilePathOfResource("employees-valid.csv");

        var lines = fileReaderService.readTableLines(resourceFilePath);
        assertThat(lines).hasSize(6);

        assertThat(lines.getFirst()).hasSize(5);
    }

    @Test
    void rejectReadingFileWithNullFileNameTest() {
        assertThatThrownBy(() -> fileReaderService.readTableLines(null))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("The filename is blank!");
    }

    @Test
    void rejectReadingFileWithBlankFileNameTest() {
        assertThatThrownBy(() -> fileReaderService.readTableLines(" "))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("The filename is blank!");
    }

    @Test
    void rejectReadingFileWithInvalidFileNameTest() {
        assertThatThrownBy(() -> fileReaderService.readTableLines("invalid-dummy.csv"))
                .isExactlyInstanceOf(UncheckedIOException.class);
    }
}
