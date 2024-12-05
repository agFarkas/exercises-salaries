package hu.epam.test.exercise.io.connection.service;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderService extends AbstractInputReaderService {

    private String fileName;

    @Override
    protected FileInputStream makeInputStream() throws IOException {
        return new FileInputStream(fileName);
    }

    public FileReaderService setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
