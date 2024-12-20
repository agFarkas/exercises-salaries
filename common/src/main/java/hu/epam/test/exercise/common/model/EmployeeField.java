package hu.epam.test.exercise.common.model;

/**
 * Defines the accepted case-sensitive column names and their required order in the raw data resource
 */
public enum EmployeeField {
    ID("Id"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    SALARY("salary"),
    MANAGER_ID("managerId");

    private final String fieldName;

    EmployeeField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
