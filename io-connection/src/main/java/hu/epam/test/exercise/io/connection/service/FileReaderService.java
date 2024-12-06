package hu.epam.test.exercise.io.connection.service;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Obtains the content of the data file as a list of lines split for separate string values in order
 */
public class FileReaderService extends AbstractInputReaderService {

    /**
     * Initiates the {@link FileInputStream} by the fileName
     *
     * @param fileName
     * @return {@link FileInputStream}
     * @throws IOException
     */
    @Override
    protected final FileInputStream makeInputStream(String fileName) throws IOException {
        return new FileInputStream(fileName);
    }
}
