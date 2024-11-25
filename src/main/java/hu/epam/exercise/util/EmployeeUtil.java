package hu.epam.exercise.util;

import hu.epam.exercise.model.EmployeeField;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeUtil {

    public static final String FIELD_NAMES_JOINED = Stream.of(EmployeeField.values())
            .map(EmployeeField::getFieldName)
                .collect(Collectors.joining(", "));

    public static List<String> getFieldNames() {
        return Stream.of(EmployeeField.values())
                .map(EmployeeField::getFieldName)
                .toList();
    }

    public static int indexOfFieldName(EmployeeField fieldName) {
        var fieldNames = EmployeeField.values();

        for (var i = 0; i < fieldNames.length; i++) {
            if (fieldNames[i] == fieldName) {
                return i;
            }
        }
        return -1;
    }

    public static int getNumberOfFieldNames() {
        return EmployeeField.values().length;
    }

    public static List<String> getHeaderLine(List<String[]> lines) {
        return List.of(lines.getFirst());
    }

    public static List<String[]> getEmployeeLines(List<String[]> lines) {
        return lines.subList(1, lines.size());
    }

    public static String getValue(String[] employeeLine, EmployeeField fieldName) {
        return employeeLine[indexOfFieldName(fieldName)];
    }
}
