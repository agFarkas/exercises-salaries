package hu.epam.test.exercise.io.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static hu.epam.test.exercise.common.DelimiterConstants.DELIMITER_COMMA;

public class TestParent {

    protected List<String[]> readTableLines(String fileName) {
        var file = new File(getAbsoluteFilePathOfResource(fileName));

        try (var scanner = new Scanner(new FileInputStream(file))) {
            var lines = new LinkedList<String[]>();

            while (scanner.hasNextLine()) {
                var lineArray = scanner.nextLine()
                        .split(DELIMITER_COMMA);
                lines.add(lineArray);
            }

            return lines;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected String getAbsoluteFilePathOfResource(String fileName) {
        return Objects.requireNonNull(TestParent.class.getClassLoader()
                        .getResource(fileName))
                .getFile();
    }

}
