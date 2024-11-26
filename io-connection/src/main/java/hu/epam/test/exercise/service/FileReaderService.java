package hu.epam.test.exercise.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Service
public class FileReaderService {

    private static final String DELIMITER = ",";

    public List<String[]> readTableLines(String fileName) {
        if (!StringUtils.hasText(fileName)) {
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
