package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.common.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static hu.epam.test.exercise.common.DelimiterConstants.DELIMITER_COMMA;

public abstract class AbstractInputReaderService {

    /**
     * Reads the table of employees.
     *
     * @param instruction Used to initiate the input stream. E.g. file name when the subclass implements a file reading by the input stream.
     * @return the list of lines from the input split for separate raw string data values by comma.
     */
    public List<String[]> readTableLines(String instruction) {
        try (var scanner = new Scanner(makeInputStream(instruction))) {
            var lines = new LinkedList<String[]>();

            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();

                if (!StringUtil.isBlank(line)) {
                    var lineArray = line.split(DELIMITER_COMMA);
                    lines.add(lineArray);
                }
            }

            return lines;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * Initiates a specific {@link InputStream}.
     *
     * @param instruction
     * @return The specific input stream based on the instruction. E.g. for {@link java.io.FileInputStream}, instruction
     * should convey the file path.
     * @throws IOException
     */
    protected abstract InputStream makeInputStream(String instruction) throws IOException;
}
