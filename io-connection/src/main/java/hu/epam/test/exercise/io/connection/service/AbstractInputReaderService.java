package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.common.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractInputReaderService {

    private static final String DELIMITER = ",";

    public List<String[]> readTableLines() {
        try (var scanner = new Scanner(makeInputStream())) {
            var lines = new LinkedList<String[]>();

            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();

                if (!StringUtil.isBlank(line)) {
                    var lineArray = line.split(DELIMITER);
                    lines.add(lineArray);
                }
            }

            return lines;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected abstract InputStream makeInputStream() throws IOException;
}
