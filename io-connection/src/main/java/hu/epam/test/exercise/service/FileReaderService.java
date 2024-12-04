package hu.epam.test.exercise.service;

import hu.epam.test.exercise.common.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileReaderService {

    private static final String DELIMITER = ",";

    public List<String[]> readTableLines(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            throw new RuntimeException("The filename is blank!");
        }

        var file = new File(fileName);

        try (var scanner = new Scanner(new FileInputStream(file))) {
            var lines = new LinkedList<String[]>();

            while (scanner.hasNextLine()) {
                var lineArray = scanner.nextLine()
                        .trim()
                        .split(DELIMITER);

                lines.add(lineArray);
            }

            return lines;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
