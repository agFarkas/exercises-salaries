package hu.epam.test.exercise.io.connection.service;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderService extends AbstractInputReaderService {

    @Override
    protected FileInputStream makeInputStream(String fileName) throws IOException {
        return new FileInputStream(fileName);
    }
}
