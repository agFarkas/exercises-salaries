package hu.epam.test.exercise.io.connection.service;

import hu.epam.test.exercise.common.exception.ValidationException;
import hu.epam.test.exercise.common.util.StringUtil;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderService extends AbstractInputReaderService {

    private static final String ERROR_MESSAGE__BLANK_FILE_NAME = "The filename is blank!";

    private final String fileName;

    public FileReaderService(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            throw new ValidationException(ERROR_MESSAGE__BLANK_FILE_NAME);
        }

        this.fileName = fileName;
    }

    @Override
    protected FileInputStream makeInputStream() throws IOException {
        return new FileInputStream(fileName);
    }
}
