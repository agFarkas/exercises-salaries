package hu.epam.exercise.util;

import hu.epam.exercise.TestParent;
import hu.epam.exercise.model.EmployeeField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeUtilTest extends TestParent {

    List<String[]> tableLines;

    @BeforeAll
    public void init() {
        this.tableLines = readTableLines("employees-valid.csv");
    }

    @Test
    void getNumberOfFieldNamesTest() {
        assertThat(EmployeeUtil.getNumberOfFieldNames())
                .isEqualTo(5);
    }

    @Test
    void getHeaderLineTest() {
        var headerLine = EmployeeUtil.getHeaderLine(tableLines);

        assertThat(headerLine).hasSize(5);
        assertThat(headerLine).containsExactly(
                EmployeeField.ID.getFieldName(),
                EmployeeField.FIRST_NAME.getFieldName(),
                EmployeeField.LAST_NAME.getFieldName(),
                EmployeeField.SALARY.getFieldName(),
                EmployeeField.MANAGER_ID.getFieldName()
        );
    }

    @Test
    void getEmployeeLinesTest() {
        var employeeLines = EmployeeUtil.getEmployeeLines(tableLines);
        assertThat(employeeLines).hasSize(5);
        assertThat(employeeLines).containsExactly(
                new String[] {"123","John","Doe","60000"},
                new String[] {"124","Martin","Chekov","45000","123"},
                new String[] {"125","Bob","Ronstad","47000","123"},
                new String[] {"300","Alice","Hasacat","50000","124"},
                new String[] {"305","Brett","Hardleaf","34000","300"}
        );
    }

    @Test
    void getValueTest() {
        var employeeLines = EmployeeUtil.getEmployeeLines(tableLines);
        var value = EmployeeUtil.getValue(employeeLines.getFirst(), EmployeeField.FIRST_NAME);
        assertThat(value).isEqualTo("John");
    }
}
