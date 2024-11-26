package hu.epam.exercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TestParent {
    private static final String DELIMITER = ",";

    protected static String getAbsoluteFilePathOfResource(String fileName) {
        return TestParent.class.getClassLoader()
                .getResource(fileName)
                .getFile();
    }

    public List<String[]> readTableLines(String fileName) {
        var file = new File(getAbsoluteFilePathOfResource(fileName));

        try (var scanner = new Scanner(new FileInputStream(file))) {
            var lines = new LinkedList<String[]>();

            while (scanner.hasNextLine()) {
                var lineArray = scanner.nextLine()
                        .split(DELIMITER);
                lines.add(lineArray);
            }

            return lines;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
