package hu.epam.test.exercise.common.util;

import hu.epam.test.exercise.common.model.EmployeeField;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.epam.test.exercise.common.DelimiterConstants.DELIMITER_COMMA_WITH_SPACE;

/**
 * Utility class to provide certain general functionalities of processing the data set about the employees.
 */
public final class EmployeeUtil {

    private EmployeeUtil() {
    }

    public static final String FIELD_NAMES_JOINED = Stream.of(EmployeeField.values())
            .map(EmployeeField::getFieldName)
            .collect(Collectors.joining(DELIMITER_COMMA_WITH_SPACE));

    /**
     * @return The list of the expected field names in the required order.
     */
    public static List<String> getFieldNames() {
        return Stream.of(EmployeeField.values())
                .map(EmployeeField::getFieldName)
                .toList();
    }

    /**
     * @param fieldName The {@link EmployeeField} enum object to retrieve its index in the specified order.
     * @return the numeric index of the passed {@link EmployeeField}.
     */
    public static int indexOfFieldName(EmployeeField fieldName) {
        var fieldNames = EmployeeField.values();

        for (var i = 0; i < fieldNames.length; i++) {
            if (fieldNames[i] == fieldName) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @return The number of field names expected in the input resource.
     */
    public static int getNumberOfFieldNames() {
        return EmployeeField.values().length;
    }

    /**
     * @param lines The entier data set as a list of raw string data values.
     * @return The header line of the data set that is required to be the first line.
     */
    public static List<String> getHeaderLine(List<String[]> lines) {
        return List.of(lines.getFirst());
    }

    /**
     * @param lines The entier data set as a list of raw string data values.
     * @return Only the employee datas (the lines of the data set except the header line)
     */
    public static List<String[]> getEmployeeLines(List<String[]> lines) {
        return lines.subList(1, lines.size());
    }

    /**
     * @param employeeLine  A separate line of raw string employee datas.
     * @param employeeField The associative reference to the field of employeeLine to return the value from.
     * @return The value at the place of the specified employeeField in the employeeLine.
     * <p>If the last index of the employeeLine passed is less than the specified index of the employeeField
     * (meaning that it is not filled), the method returns null.
     */
    public static String getValue(String[] employeeLine, EmployeeField employeeField) {
        var index = indexOfFieldName(employeeField);

        if (lastIndexInEmployeeLine(employeeLine) < index) {
            return null;
        }

        return employeeLine[index];
    }

    private static int lastIndexInEmployeeLine(String[] employeeLine) {
        return employeeLine.length - 1;
    }
}
